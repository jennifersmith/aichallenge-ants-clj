(ns ai)
(defn up-left-ai [world]
  (map
    (fn [pos]
      {:pos pos :direction "N"})
    (:my-ants world)
  ))
(def next-move up-left-ai)
