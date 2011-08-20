(ns world (:use debug))

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
 
(defn get-surrounding-coords-1 [{:keys [dimensions]} [curr-row curr-col]]
  (let [[grid-rows grid-cols] dimensions]
  (dbg (partition 3 
             (for [
                   row (map (partial + curr-row) (range -1 2)) 
                   col (map (partial + curr-col) (range -1 2))]
               [(mod row grid-rows) (mod col grid-cols)])))))
;; New stuffg
(defn get-contents [world position]
  ;; temp until I rejig world
  (if (nil? (:environment world)) (throw (Exception. "TODO - make world right... well that is a tall order but YNWIM")))
  (get (:environment world) position))
