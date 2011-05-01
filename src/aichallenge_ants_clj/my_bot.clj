(ns aichallenge-ants-clj.my-bot
  [:gen-class]
  (:use [aichallenge-ants-clj.ants] :reload))

;; ## MyBot template.
;;
;; Single method defrecord for now.
(defrecord MyBot []
  AntsBot
  ;; do-turn :: MyBot -> AntsWorld -> MyBot
  (do-turn [this world] this))

;; make-bot :: MyBot
(defn make-bot [] (MyBot.))

