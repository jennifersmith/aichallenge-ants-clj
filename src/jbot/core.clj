(ns jbot.core
  (:gen-class)
  (:require [clojure.pprint :as pp] [clojure.contrib.logging :as logs] [ clojure.contrib.string :as string])
  (:import (java.io BufferedReader)))
;; too lazy to split into files!
(defn dump [& data](spit "dump.log" (apply str (cons "\n" (interpose " " data))) :append true))
(defmacro dbg[x] `(let [x# ~x] (dump '~x "=" x#) x#))

;; string utils
(defn tokenize [line] (string/split #"\s" line))

;; Protocol parsing stuff (not as in clojure protocols...) - this stuff takes tokenized strings and gets the goodies out
;; Should I maybe assert "turn" as in "turn 2" and "end" as in the end of the game?

(def code->event-type 
  {"a" :live-ant
   "d" :dead-ant
   "f" :food
   "w" :water})

(defn read-turn-header
  ([turn-message turn-number] (Integer/parseInt turn-number))
  ([end-message] nil))


(defn read-turn-input 
  ([event-type x y player] (assoc (read-turn-input event-type x y) :player player))
  ([event-type x y] {:type (code->event-type event-type) :pos {:x (Integer/parseInt x) :y (Integer/parseInt y)}}))

;; in stream reading stuff
(defn read-upto [stop-token]
  (loop [result []]
      (let [line (read-line)] 
        (dump line)
        (if (or (nil? line) (= stop-token line))
          result
          (recur (cons line result))))))

(defn read-parameters []
  (let [lines (read-upto "ready")]
    (reduce (fn [result line] 
           (apply (partial assoc result) (tokenize line)))
           {}           
            lines)))

(defn read-turn-data []
  (let [lines (read-upto "go")]
    (map (fn [line] (apply read-turn-input (tokenize line))) lines)))

(defn read-turn []
  (if-let [turn-header (apply read-turn-header (tokenize (read-line)))]
    {:turn-number turn-header
     :turn-data (dbg (read-turn-data))}))

;; Main!
 
(defn -main [& args]
  (let [parameters (read-parameters)] 
    (do (println "go") (.flush System/out)) 
    (loop []

      (if-let [turn (read-turn)]
        (do
          (println "go")
          (.flush System/out)
          (recur))
        (do
          (read-line)
          (read-line))))))
