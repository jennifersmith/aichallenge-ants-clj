(ns environment
  (:use debug))


(defn init-environment [rows cols] {:dimensions [rows cols] :tiles{}})

(defn remove-out-of-date-info [environment]
  (let [out-of-date-tiles (for [[k v] environment :when (not= v :water)] k)] 
  (apply dissoc environment out-of-date-tiles)))

(defn increment-environment [{:keys [tiles] :as current-environment}  new-information]
  (let [previous-environment (remove-out-of-date-info tiles)]
    (assoc current-environment 
           :tiles (merge previous-environment (zipmap (map :pos new-information) (map :type new-information))))))

(defn get-surrounding-coords [{:keys [dimensions]} [curr-row curr-col]]
  (dump dimensions "dim")
  (dump curr-row curr-col " row coll")
  (let [[grid-rows grid-cols] dimensions]
  (partition 3 
             (for [
                   row (map (partial + curr-row) (range -1 2)) 
                   col (map (partial + curr-col) (range -1 2))]
               [(mod row grid-rows) (mod col grid-cols)]))))

(defn get-contents [{:keys [tiles]} position] (get tiles  position))

(defn get-available-directions [environment ant-pos]
  (let [
        [[_ N _]
         [W _ E]
         [_ S _]]
        (get-surrounding-coords  environment ant-pos)
        directions 
          {:N N :E E :S S :W W}
        contents-by-direction
          (zipmap [:N :E :S :W] (map (partial get-contents environment) [N E S W]))
        ]
   
    (map key (filter #(nil? (#{:water :food} (val %))) contents-by-direction))))

