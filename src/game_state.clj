(ns game-state 
  (:use environment debug structure)
  (:import java.util.Random))


(defn get-player-ants [my-player-name new-information]
  (map :pos
       (filter 
         (fn [{:keys [player type]}]
           (= [my-player-name :ant] [player type]))
         new-information)))




(defrecord PlayerAnts [player-name ants]
  IncrementableState
  (increment-state [this new-information]
                   (let [
                         new-ants (get-player-ants player-name new-information)
                         ]
                     (assoc this :ants new-ants))))


(defrecord GameState [my-ants random-generator environment]
  IncrementableState
  (increment-state [this new-info] 
                   (assoc
                     this
                     :environment (increment-state environment new-info)
                     :my-ants (increment-state my-ants new-info))))

(def -seeded-rand-generator (atom nil))
(defn make-rand-generator [seed]
  (Random. seed))
(defn seed-random-generator [seed]
  (swap! -seeded-rand-generator (constantly (make-rand-generator seed)))
  (fn [max]
    (if (> max 0)
      (.nextInt @-seeded-rand-generator max)
      0)))


(defn init-my-ants [player] (PlayerAnts. player []))
(defn init-random-generator [seed] (seed-random-generator seed))

(defn init-game-state 
  [{:keys [rows cols player_seed] :as params}] 
  (GameState.
    (init-my-ants "0")
    (init-random-generator player_seed)
    (init-environment rows cols))) 


