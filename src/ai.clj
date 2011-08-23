(ns ai
  (:use debug)
  (:require game-state))

(defn ant-next-move [game-state rand-generator pos]
  (let [directions  (game-state/get-available-directions game-state pos)]
  {:pos pos :direction (nth (vec directions) (rand-generator (count directions)) nil)}))

(defn sample-bot-move [{:keys [my-ants] :as game-state}] 
  (filter (comp :direction identity) (map (partial ant-next-move game-state (:rand-generator game-state)) my-ants)))

;; let's hear it for plugability
(def next-move sample-bot-move)
