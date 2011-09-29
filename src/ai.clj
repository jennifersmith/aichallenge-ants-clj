(ns ai
  (:use environment debug))

(defn preferred-moves [current-pos history  available-directions]
(dbg  (vec (map key
       (if (or (= 1 (count available-directions)) (empty? history))
         available-directions
         (let
           [last-positions (set (take-last 10 (:positions history)))
            visited-positions-from-available (map key (filter #(last-positions (val %)) available-directions ))  ] 
            (reduce
              (fn [directions historic-position]
                (if (> 1 (count directions))
                  (dissoc directions historic-position)
                  directions)
                  
                )
                  available-directions 
                  last-positions)
              available-directions
            ))))))

(defn ant-next-move [{:keys [pos directions history]}]
  (if (empty? directions)
    nil
    {:pos pos :direction (dbg (rand-nth (preferred-moves pos history directions)))}))



(defn move-ants [ history {:keys [my-ants environment random-generator]}]
  (dbg "We got ants")
  (dbg (count (:ants  my-ants)))
  (filter
    identity
    (map
      ant-next-move 
      (map
        (fn [ant-pos]
          {
           :pos ant-pos
           :history (or (get history ant-pos) {})
           :directions (get-available-directions environment ant-pos)})
        (:ants my-ants)))))
;; let's hear it for plugability
(def next-move move-ants)
