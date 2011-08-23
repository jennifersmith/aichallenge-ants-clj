(ns ai
  (:use debug)
  (:import java.util.Random) 
  (:require game-state))

(def -seeded-rand-generator (atom nil))
(defn make-rand-generator [seed]
  (Random. seed))
(defn seed-rand-generator [seed]
  (swap! -seeded-rand-generator (constantly (make-rand-generator seed)))
  (fn [max]
    (if (> max 0)
      (.nextInt @-seeded-rand-generator max)
      0)))

(defn sample-bot-init [game-state] (assoc game-state :rand-generator (seed-rand-generator (:rand-seed game-state))))

(defn get-available-directions [game-state ant-pos]
  (let [
        [[_ N _]
         [W _ E]
         [_ S _]]
        (game-state/get-surrounding-coords  game-state ant-pos)
        directions 
          {:N N :E E :S S :W W}
        contents-by-direction
          (zipmap [:N :E :S :W] (map (partial game-state/get-contents game-state) [N E S W]))
        ]
   
    (map key (filter #(nil? (#{:water :food} (val %))) contents-by-direction))))

(defn ant-next-move [game-state rand-generator pos]
  (let [directions  (get-available-directions game-state pos)]
  {:pos pos :direction (nth (vec directions) (rand-generator (count directions)) nil)}))

(defn sample-bot-move [{:keys [my-ants] :as game-state}] 
  (filter (comp :direction identity) (map (partial ant-next-move game-state (:rand-generator game-state)) my-ants)))
;; let's hear it for plugability
(def next-move sample-bot-move)
(def init sample-bot-init)
