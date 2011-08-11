(ns jbot.core
  (:gen-class)
  (:require [clojure.pprint :as pp] [clojure.contrib.logging :as logs] [ clojure.contrib.string :as string])
  (:import (java.io BufferedReader)))

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
           (apply (partial assoc result) (string/split #"\s" line)))
           {}           
            lines)))

(defn -main [& args]
  (let [parameters (read-parameters *in*)] "todo : play game"))
