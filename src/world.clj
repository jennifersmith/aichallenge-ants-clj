(ns world (:use debug))

(defn init-world 
  [{:keys [rows cols player-seed] :as params}] 
  {:player-name "0" :rand-seed player-seed  :dimensions [(Integer/parseInt rows) (Integer/parseInt cols)]  :environment{} :my-ants[]})

(defn get-player-ants [my-player-name new-information]
  (map :pos
       (filter 
         (fn [{:keys [player type]}]
           (= [my-player-name :ant] [player type]))
         new-information)))

(defn remove-out-of-date-info [environment]
  (let [out-of-date-tiles (for [[k v] environment :when (not= v :water)] k)] 
  (apply dissoc environment out-of-date-tiles)))

(defn increment-world [current-state new-information]
  (let [
        previous-environment (remove-out-of-date-info (:environment current-state))
        new-environment (merge previous-environment (zipmap (map :pos new-information) (map :type new-information)))
        player-name (:player-name current-state)
        ants (get-player-ants player-name new-information)
        ]
    (-> current-state
      (assoc :environment new-environment)
      (assoc :my-ants ants))))


(defn get-surrounding-coords [{:keys [dimensions]} [curr-row curr-col]]
  (let [[grid-rows grid-cols] dimensions]
  (partition 3 
             (for [
                   row (map (partial + curr-row) (range -1 2)) 
                   col (map (partial + curr-col) (range -1 2))]
               [(mod row grid-rows) (mod col grid-cols)]))))

(defn get-contents [world position] (get (:environment world) position))
