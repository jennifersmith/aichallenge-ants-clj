(ns core (:use debug parsing world ai))
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

  (defn foo []
  (if-let [turn-header (apply read-turn-header (read-one))]
    {:turn-number turn-header
     :turn-data (seq (map (partial apply parse-turn-input) (read-upto "go")))}))
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
    (loop [world (init (init-world parameters))] ;; INIT is ai init and init-world is core I think. Dodgy!
      (if-let [turn (read-turn)] 
        (let [world (increment-world world (:turn-data turn))]
          (doseq [line (map render-move (next-move world))]
            (dump (vec (:environment world)))
            (dump line)
            (println line)
            )
          (println "go")
          (.flush System/out)
          (recur world))
        (do
          (read-line)
          (read-line))))))
