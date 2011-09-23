(ns ai
  (:use environment debug))

(defn is-looping [seq]
 (dbg seq)
 (dbg (let [[a b &rest]  (partition 2 (reverse seq))] (= a b))))
(defn remove-loops [previous-directions directions]
  (let 
    [previous-directions (vec previous-directions)]
  (map
    last
    (filter (comp not is-looping) (map (partial conj previous-directions) directions)))))

(defn preferred-moves [history  available-directions]
  (dbg available-directions)
  (if (or (= 1 (count available-directions)) (empty? history))
    available-directions
    (remove-loops (:directions history) available-directions)))

(defn ant-next-move [{:keys [pos directions history]}]
  (dbg "ANT NEXT MOVE")
  (dbg pos)
  (dbg (vec directions))
  (if (empty? directions)
    nil
    {:pos pos :direction (dbg (rand-nth (preferred-moves history directions)))}))



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
