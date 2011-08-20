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

;; good stuff

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
(defn sample-bot [& whatever] "FOO")
;; let's hear it for plugability
(def next-move sample-bot)
