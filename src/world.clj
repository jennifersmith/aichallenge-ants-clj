(ns world)

(defn increase-knowledge-of [knowledge-type parameters current-knowledge]
  (concat current-knowledge (map :pos (filter #(= knowledge-type (:type %)) parameters))))


