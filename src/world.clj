(ns world)
(defn dump-1 [& data] (println (apply str data)))

(defmacro dbg-1[x] `(let [x# ~x] (dump-1 '~x "=" x#) x#))
(defn pivot [filter-key required-values data]
  (zipmap required-values (map
    (fn [current-key]
      (filter
        (fn [current] (= current-key (get current filter-key)))
        data))
    required-values)))

(defn init-world [] {:player-name "0" :food [] :water [] :my-ants[]})

(defn increment-world [current-state new-information]
  (let [
        new-information-by-type (pivot :type [:food :water :ant] new-information)
        new-water (map :pos (:water new-information-by-type))
        new-food (map :pos (:food new-information-by-type))
        player-name (:player-name current-state)
        ants (map :pos (filter #(= player-name (:player  %)) (:ant new-information-by-type)))
        ]
    (-> current-state
      (assoc :water (concat (:water current-state) new-water))
      (assoc :food new-food)
      (assoc :my-ants ants))))

