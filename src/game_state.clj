(ns game-state 
  (:use environment debug)
  (:import java.util.Random))

(def -seeded-rand-generator (atom nil))
(defn make-rand-generator [seed]
  (Random. seed))
(defn seed-random-generator [seed]
  (swap! -seeded-rand-generator (constantly (make-rand-generator seed)))
  (fn [max]
    (if (> max 0)
      (.nextInt @-seeded-rand-generator max)
      0)))


(defn init-my-ants [player] {:player-name player :ants[]})
(defn init-random-generator [seed] (seed-random-generator seed))

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


   
(defn increment-my-ants [current-ants new-information]
  (let [
        player-name (:player-name current-ants)
        ants (get-player-ants player-name new-information)
        ]
      (assoc current-ants :ants ants)))


(defn increment-game-state [current-game-state new-info] 
  (assoc
    current-game-state
    :environment (increment-state (:environment current-game-state) new-info)
    :my-ants (increment-my-ants (:my-ants current-game-state) new-info)))


