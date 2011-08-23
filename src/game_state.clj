(ns game-state (:use debug))

(defn init-environment [rows cols] {:dimensions [rows cols] :tiles{}})
(defn init-my-ants [player] {:player-name player :ants[]})
(defn init-random-generator [seed] :fibble)

(defn init-game-state 
  [{:keys [rows cols player_seed] :as params}] 
  {
   :my-ants (init-my-ants "0")
   :random-generator (init-random-generator (Long/parseLong player_seed))
   :environment (init-environment (Integer/parseInt rows) (Integer/parseInt cols))})

(defn get-player-ants [my-player-name new-information]
  (map :pos
       (filter 
         (fn [{:keys [player type]}]
           (= [my-player-name :ant] [player type]))
         new-information)))


(defn remove-out-of-date-info [environment]
  (let [out-of-date-tiles (for [[k v] environment :when (not= v :water)] k)] 
  (apply dissoc environment out-of-date-tiles)))

(defn increment-environment [{:keys [tiles] :as current-environment}  new-information]
  (let [previous-environment (remove-out-of-date-info tiles)]
    (assoc current-environment 
           :tiles (merge previous-environment (zipmap (map :pos new-information) (map :type new-information))))))
    
(defn increment-my-ants [current-ants new-information]
  (let [
        player-name (:player-name current-ants)
        ants (get-player-ants player-name new-information)
        ]
      (assoc current-ants :ants ants)))


(defn increment-game-state [current-game-state new-info] 
  (assoc
    current-game-state
    :environment (increment-environment (:environment current-game-state) new-info)
    :my-ants (increment-my-ants (:my-ants current-game-state) new-info)))


(defn get-surrounding-coords [{:keys [dimensions]} [curr-row curr-col]]
  (let [[grid-rows grid-cols] dimensions]
  (partition 3 
             (for [
                   row (map (partial + curr-row) (range -1 2)) 
                   col (map (partial + curr-col) (range -1 2))]
               [(mod row grid-rows) (mod col grid-cols)]))))

(defn get-contents [{:keys [tiles]} position] (get tiles  position))
