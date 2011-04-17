(ns aichallenge-ants-clj.core
  [:gen-class]
  [:use aichallenge-ants-clj.ants]
  [:use aichallenge-ants-clj.my-bot])


;; [& strings] -> nil
(defn -main [& args]
  (let [[world bot] (run (make-world) (make-bot))
        last-world (update world)]
    (do
      (println world)
      (println bot)
      (println last-world))))

