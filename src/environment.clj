(ns environment
  (:use debug structure))

(def compass->vector 
  {
   :N [-1 0]
   :S [1 0]
   :E [0 1]
   :W [0 -1]
   })


(defn remove-out-of-date-info [environment]
  (let [out-of-date-tiles (for [[k v] environment :when (not= v :water)] k)] 
    (apply dissoc environment out-of-date-tiles)))

(defprotocol NavigablePlane
  (get-surrounding-coords [this point])
  (get-contents [this position])
  (wrap-coords [this [x y]])
  (translate-pos [this {:keys [pos direction]}])
  (get-available-directions [this [row col]]))

(defrecord Environment [dimensions tiles]       
  IncrementableState
  (increment-state [this  new-information]
                   (let [previous-environment (remove-out-of-date-info tiles)]
                     (assoc this 
                            :tiles (merge previous-environment (zipmap (map :pos new-information) (map :type new-information))))))
  NavigablePlane
  (wrap-coords [this [row col]]
               (let [[grid-rows grid-cols] dimensions]
                [(mod row grid-rows) (mod col grid-cols)])    
               )
  (get-surrounding-coords [this [curr-row curr-col]]
                         
                                 (partition 3 
                                            (for [
                                                  row (map (partial + curr-row) (range -1 2)) 
                                                  col (map (partial + curr-col) (range -1 2))]
                                              (wrap-coords this [row col])
                                              )))
  (get-contents [this position] (get tiles  position))
  (get-available-directions [this position]
                            (let [
                                  [[_ N _]
                                   [W _ E]
                                   [_ S _]]
                                  (dbg (get-surrounding-coords  this  position))
                                  directions {:N N :E E :S S :W W}
                                  
                                  contents-by-direction
                                  (zipmap [:N :E :S :W] (map (partial get-contents this) [N E S W]))
                                  ]
                              
                                      
                              (filter (fn [[dir pos]] (nil? ( #{:water :food} (get-contents this pos)))) directions)))

  (translate-pos [this {:keys [pos direction]}]
    (wrap-coords this 
    (map + pos (get compass->vector direction))))

  )

(defn init-environment [rows cols] (Environment. [rows cols] {}))
