(ns ai)

(defn get-next-passable-direction [surroundings]
  "N"
  )  
 
(defn sample-bot[world]
  (map
    (fn [ant-pos]
      {:pos ant-pos :direction (get-next-passable-direction (get-surroundings world ant-pos))})
    (:my-ants world)))
 
;; let's hear it for plugability
(def next-move sample-bot)
