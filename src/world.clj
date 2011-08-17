(ns world)
(defn dump-1 [& data] (println (apply str data)))

(defmacro dbg-1[x] `(let [x# ~x] (dump-1 '~x "=" x#) x#))
(defn pivot [filter-key required-values data]
  (zipmap required-values (map
    (fn [current-key]
      (filter
        (fn [current] (= current-key (get current filter-key)))
        data))
    required-values)))

(defn init-world 
  [{:keys [rows cols] :as params}] 
  {:player-name "0" :dimensions [(Integer/parseInt rows) (Integer/parseInt cols)]  :food [] :water [] :my-ants[]})

(defn increment-world [current-state new-information]
  (let [
        new-information-by-type (pivot :type [:food :water :ant] new-information)
        new-water (map :pos (:water new-information-by-type))
        new-food (map :pos (:food new-information-by-type))
        player-name (:player-name current-state)
        ants (map :pos (filter #(= player-name (:player  %)) (:ant new-information-by-type)))
        ]
    (-> current-state
      (assoc :water (concat (:water current-state) new-water))
      (assoc :food new-food)
      (assoc :my-ants ants))))


(defn get-surrounding-coords [[grid-rows grid-cols] [curr-row curr-col]]
  (partition 3 
             (for [
                   row (map (partial + curr-row) (range -1 2)) 
                   col (map (partial + curr-col) (range -1 2))]
               [(mod row grid-rows) (mod col grid-cols)])))
    
;; better impl later
(defn get-contents-for-row [environment row]
  (map (fn [point]
         (if ((set (:water environment)) point)
           :water
           nil))
        row))
(defn get-surroundings [world position]
  (let 
    [
     environment { :water (:water world)}] ;; todo make the sets of points a sub property of world ratehr than having water just on it
    (map
      (partial get-contents-for-row environment)
      (get-surrounding-coords (:dimensions world) position))))
