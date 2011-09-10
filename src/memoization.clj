(ns memoization (:use environment))

(defn direction-history [current {:keys [pos direction]}]
  (let [previous-history 
        (get current pos)]
  (merge-with concat previous-history
  {:directions [direction]}
  )))

(defn save-ant-state [environment current new-states]
  (zipmap (map (partial translate-pos environment) new-states) (map (partial direction-history current) new-states)))
