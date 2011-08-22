(ns ai
  (:use debug)
  (:import java.util.Random) 
  (:require world))

(def -seeded-rand-generator (atom nil))
(defn make-rand-generator [seed]
  (Random. seed))
(defn seed-rand-generator [seed]
  (swap! -seeded-rand-generator (constantly (make-rand-generator seed)))
  (fn [max]
    (.nextInt @-seeded-rand-generator max)))

(defn sample-bot-init [world] (assoc world :rand-generator (seed-rand-generator (:rand-seed world))))

(defn seeded-rand [max]
  1
  )
(defn get-available-directions [world ant-pos]
  (let [
        [[_ N _]
         [W _ E]
         [_ S _]]
        (world/get-surrounding-coords  world ant-pos)
        directions 
          {:N N :E E :S S :W W}
        contents-by-direction
          (zipmap [:N :E :S :W] (map (partial world/get-contents world) [N E S W]))
        ]
   
    (map key (filter #(nil? (#{:water :food} (val %))) contents-by-direction))))

(defn ant-next-move [world rand-generator pos]
  (let [directions  (get-available-directions world pos)]
  {:pos pos :direction (nth (vec directions) (rand-generator (count directions)) nil)}))

(defn sample-bot-move [{:keys [my-ants] :as world}] 
  (filter (comp :direction identity) (map (partial ant-next-move world (:rand-generator world)) my-ants)))
;; let's hear it for plugability
(def next-move sample-bot-move)
(def init sample-bot-init)
