(ns core (:use debug parsing game-state ai structure))
;; in stream reading stuff
(defn tokenize [line] (seq (.split #"\s" line)))
(defn read-input []
  (map tokenize (repeatedly read-line)))

(defn read-one [] (first (read-input)))

(defn read-upto [& stop-tokens]
  (doall (take-while #(empty? (clojure.set/intersection (set stop-tokens) (set %))) (read-input))))

;; combining parsing and reading stream stuff
(defn read-parameters []
  (let [lines (read-upto "ready")]
    (reduce (fn [result [k v]] 
           (assoc result (keyword k) v))
           {}           
            lines)))

(defn read-turn []
  (parse-turn (read-upto "go" "end")))

  ;; and finally... render!

(defn render-move [{:keys [pos direction]}]
  (str 
    "o "
    (apply str (interpose " " pos))
    " "
    (.toUpperCase (name direction))))
;; Main!
;; evil game loop
(defn -main [& args]
  (let [parameters (read-parameters) ]
    (do (println "go") (.flush System/out)) 
    (loop [game-state (init-game-state parameters)] ;; INIT is ai init and init-game-state is core I think. Dodgy!
      (if-let [turn (read-turn)] 
        (let [game-state (increment-state game-state (:turn-data turn))]
          (doseq [line (map render-move (next-move game-state))]
            (dump (vec (:environment game-state)))
            (dump line)
            (println line)
            )
          (println "go")
          (.flush System/out)
          (recur game-state))
        (do
          (read-line)
          (read-line))))))
