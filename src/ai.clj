(ns ai
  (:use debug)
  (:require game-state))

(defn ant-next-move [environment random-generator pos]
  (let [directions  (game-state/get-available-directions environment pos)]
  {:pos pos :direction (nth (vec directions) (random-generator (count directions)) nil)}))

(defn sample-bot-move [{:keys [my-ants environment random-generator]}] 
  (filter (comp :direction identity) (map (partial ant-next-move environment random-generator) my-ants)))

;; let's hear it for plugability
(def next-move sample-bot-move)
