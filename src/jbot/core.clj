(ns jbot.core
  (:gen-class)
  (:require [clojure.pprint :as pp] [clojure.contrib.logging :as logs] [ clojure.contrib.string :as string])
  (:import (java.io BufferedReader)))
(defn tokenize [line]
  (string/split #"\s" line))

(defn read-upto [stop-token reader]
  (let [reader (new BufferedReader reader)]
    (loop [result []]
      (let [line (.readLine reader)]
        (if (or (nil? line) (= stop-token line))
          result
          (recur (cons line result)))))))

(defn read-parameters [reader]
  (let [lines (read-upto "ready" reader)]
    (reduce (fn [result line] 
           (apply (partial assoc result) (tokenize line)))
           {}           
            lines)))

(def code->event-type 
  {"a" :live-ant
   "d" :dead-ant
   "f" :food
   "w" :water})

(defn read-turn-input 
  ([event-type x y player] (assoc (read-turn-input event-type x y) :player player))
  ([event-type x y] {:type (code->event-type event-type) :pos {:x (Integer/parseInt x) :y (Integer/parseInt y)}}))

(defn read-turn-data [reader]
  (let [lines (read-upto "go" reader)]
    (map (fn [line] (apply read-turn-input (tokenize line))) lines)))

;; Should I maybe assert "turn" as in "turn 2" and "end" as in the end of the game?
(defn read-turn-header 
  ([turn-message turn-number] (Integer/parseInt turn-number))
  ([end-message] nil))

(defn read-turn [reader]
  (if-let [turn-header (apply read-turn-header (tokenize (.readLine (new BufferedReader reader))))]
    {:turn-number turn-header
     :turn-data (read-turn-data reader)}))

(defn -main [& args]
  (let [parameters (read-parameters *in*)] 
    (println "go")
    (loop []
      (if-let [turn (read-turn *in*)]
        (do
          (println "go")
          (recur))
        (do
          (read-line)
          (read-line))))))
