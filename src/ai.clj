(ns ai
  (:use environment debug))

(defn ant-next-move [environment random-generator pos]
  (let [directions  (get-available-directions environment pos)]
  {:pos pos :direction (nth (vec directions) (random-generator (count directions)) nil)}))

(defn sample-bot-move [{:keys [my-ants environment random-generator]}] 
  (filter (comp :direction identity) (map (fn [ant-pos] (ant-next-move environment random-generator ant-pos)) (:ants my-ants))))

;; let's hear it for plugability
(def next-move sample-bot-move)
