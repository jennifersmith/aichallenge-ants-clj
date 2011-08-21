(ns ai
  (use debug)
  (:require world))
(defn get-available-directions [world ant-pos]
  (let [
        [[_ N _]
         [W _ E]
         [_ S _]]
        (dbg (world/get-surrounding-coords  world ant-pos))
        directions 
          {:N N :E E :S S :W W}
        contents-by-direction
          (zipmap [:N :E :S :W] (map (partial world/get-contents world) [N E S W]))
        ]
   
    (map key (filter (comp nil? second) contents-by-direction))))

(defn ant-next-move [world pos]
  {:pos pos :direction (first (get-available-directions world pos))})

(defn sample-bot-move [{:keys [my-ants] :as world}] 
  (filter (comp :direction identity) (map (partial ant-next-move world) my-ants)))
;; let's hear it for plugability
(def next-move sample-bot-move)
