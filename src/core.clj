(ns core (:use world ai) (:import (java.io BufferedReader)))

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
  ([end-message] nil)
  ([too many & args] (throw (Exception. (apply str (flatten (cons [too many] args)))))))


(defn parse-turn-input 
  ([event-type x y player] (assoc (parse-turn-input event-type x y) :player player))
  ([event-type x y] {:type (code->event-type event-type) :pos (vec (map parse-int  [x y]))}))

;; in stream reading stuff
;; for ever and ever and ever
(defn read-input []
  (map tokenize (repeatedly read-line)))
(defn read-one [] (first (read-input)))

(defn read-upto [stop-token]
  (doall (take-while #(not (some #{stop-token} %)) (read-input))))
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
     :turn-data (seq (map (partial apply parse-turn-input) (read-upto "go")))}))
;; and finally... render!

(defn render-move [{:keys [pos direction]}]
  (str 
    "o "
    (apply str (interpose " " pos))
    " "
    direction))

;; Main!
(defn -main [& args]
  (let [parameters (read-parameters) ] 
    (do (println "go") (.flush System/out)) 
    (loop [world (init-world parameters)]
      (if-let [turn (read-turn)] 
        (do
          (doseq [line (map render-move (next-move world))]
            (println line)
            )
          (println "go")
          (.flush System/out)
          (recur (increment-world world (:turn-data turn))))
        (do
          (read-line)
          (read-line))))))
