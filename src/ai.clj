(ns ai
  (use debug)
  (:require world))
;; The whole point of this exercise
(defn get-next-passable-direction
  [[
   [_ N _]
   [W _ E]
   [_ S _]
   ]]
  (let
    [
      directions {"N" N "E" E "S" S "W" W}]
   (first (map key 
     (filter (fn [[k v]] (nil? v)) directions))))) ;; hackety hack
(defn sample-bot[world]
  (filter
    (fn [{:keys [direction]}] (not (nil? direction)))
  (map
    (fn [ant-pos]
      (dump "FOR " ant-pos "coords: " (seq (world/get-surrounding-coords (:dimensions world) ant-pos))  " surroundings: "   (seq (world/get-surroundings world ant-pos)))
      {:pos ant-pos :direction (get-next-passable-direction (world/get-surroundings world ant-pos))})
    (:my-ants world))))
;; let's hear it for plugability
(def next-move sample-bot)
