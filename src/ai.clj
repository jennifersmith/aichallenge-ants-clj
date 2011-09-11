(ns ai
  (:use environment debug))

(defn is-looping [seq]
  false)

(defn remove-loops [previous-directions potential-directions]
  potential-directions)
  (comment
  (if (nil? previous-directions)
    directions
  (filter #(not (is-looping (conj previous-directions %))) directions)))


(defn preferred-moves [history  available-directions]
  (if (= 1 (count available-directions))
    available-directions
    (remove-loops (:directions history) available-directions)))

(defn calculate-directions [random-generator directions history]
  (if (= 1 (count directions)) 
    (first directions)
    (let [directions (remove-loops directions (:directions history))]
      (nth (vec directions) (random-generator (count directions))))))

(defn ant-next-move [{:keys [pos directions history]}]
  (if (empty? directions)
    nil
    {:pos pos :direction (rand-nth (preferred-moves history directions))}))
   ;; {:pos pos :direction (calculate-directions random-generator directions history)}))



(defn move-ants [ history {:keys [my-ants environment random-generator]}]
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
