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
  {:player-name "0" :dimensions [(Integer/parseInt rows) (Integer/parseInt cols)]  :environment{} :my-ants[]})

(defn get-player-ants [my-player-name new-information]
  (map :pos
       (filter 
         (fn [{:keys [player type]}]
           (= [my-player-name :ant] [player type]))
         new-information)))

(defn increment-world [current-state new-information]
  (let [
        new-environment (zipmap (map :pos new-information) (map :type new-information)) 
        player-name (:player-name current-state)
        ants (get-player-ants player-name new-information)
        ]
    (-> current-state
      (assoc :environment new-environment)
      (assoc :my-ants ants))))


(defn get-surrounding-coords [{:keys [dimensions]} [curr-row curr-col]]
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
