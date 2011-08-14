(ns world)

(defn get-knowledge [knowledge-type new-information]
  (map :pos (filter #(= knowledge-type (:type %)) new-information)))
(defn increase-knowledge-of [knowledge-type new-information current-knowledge]
  (concat current-knowledge (get-knowledge knowledge-type new-information)))

(defn forget-about [knowledge-type new-information current-knowledge]
  (remove (set (get-knowledge knowledge-type new-information)) current-knowledge))
