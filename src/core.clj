(ns core (:import (java.io BufferedReader)))

;; too lazy to split into files!
(defn dump [& data](spit "dump.log" (apply str (cons "\n" (interpose " " data))) :append true))
(defmacro dbg[x] `(let [x# ~x] (dump '~x "=" x#) x#))

;; string utils
(defn tokenize [line] (seq (.split #"\s" line)))
(defn parse-int [value] (Integer/parseInt value)) ;; I dont think can use a java function as a clojure fn right in map and stuff like that?

;; Protocol parsing stuff (not as in clojure protocols...) - this stuff takes tokenized strings and gets the goodies out
;; Should I maybe assert "turn" as in "turn 2" and "end" as in the end of the game?

(def code->event-type 
  {"a" :ant
   "d" :dead-ant
   "f" :food
   "w" :water})

(defn read-turn-header
  ([turn-message turn-number] (Integer/parseInt turn-number))
  ([end-message] nil))


(defn parse-turn-input 
  ([event-type x y player] (assoc (parse-turn-input event-type x y) :player player))
  ([event-type x y] {:type (code->event-type event-type) :pos (vec (map parse-int  [x y]))}))

;; in stream reading stuff
;; for ever and ever and ever
(defn read-input []
  (map tokenize (repeatedly read-line)))
(defn read-one [] (first (read-input)))

(defn read-upto [stop-token]
  (take-while #(not (some #{stop-token} %)) (read-input)))

;; combining parsing and reading stream stuff
(defn read-parameters []
  (let [lines (read-upto "ready")]
    (reduce (fn [result line] 
           (apply (partial assoc result) line))
           {}           
            lines)))

(defn read-turn []
  (if-let [turn-header (apply read-turn-header (read-one))]
    {:turn-number turn-header
     :turn-data (map (partial apply parse-turn-input) (read-upto "go"))}))

;; Main!
(defn -main [& args]
  (let [parameters (read-parameters)] 
    (do (println "go") (.flush System/out)) 
    (loop []

      (if-let [turn (read-turn)]
        (do
          (dump turn)
          (println "go")
          (.flush System/out)
          (recur))
        (do
          (read-line)
          (read-line))))))
