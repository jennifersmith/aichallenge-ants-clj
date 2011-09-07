(ns ai
  (:use environment debug))

(defn ant-next-move [ environment random-generator {:keys [pos directions]}]
  (if (empty? directions)
    nil
    {:pos pos :direction (nth (vec directions) (random-generator (count directions)))}))

(defn move-ants [{:keys [my-ants environment random-generator]}]
  (filter
    identity
    (map
      (partial ant-next-move environment random-generator)
      (map
        (fn [ant-pos]
          {
           :pos ant-pos
           :directions (get-available-directions environment ant-pos)})
        (:ants my-ants)))))
;; let's hear it for plugability
(def next-move move-ants)
