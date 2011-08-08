(ns my-bot.core
  (:gen-class)
  (:require [clojure.pprint :as pp] [clojure.contrib.logging :as logs])
  (:use my-bot.ants my-bot.ai))

(defn -main [& args]
  (let [parameters (read-parameters *in*)]))
;; ## Main loop.
;;
;; Entry point of the program. If you keep to the template this does
;; not need to be changed.
;;
;; -main :: [[String]] -> nil

(defn -main1 [& args]
  (let [[world bot] (run (make-world) (make-bot))
        last-world (update world)]
    (do
      (println "initial ==")
      (pp/pprint world)
      (println "final bot ==")
      (pp/pprint bot)
      (println "final world ==")
      (pp/pprint last-world)
      (println))))

