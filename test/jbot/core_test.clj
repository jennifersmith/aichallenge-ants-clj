(ns jbot.core_test
  (:use 
        jbot.core
        clojure.test
        midje.sweet)
  (:require [clojure.string :as string])
  (:import (java.io StringReader)))

(defn create-reader [& lines]
  (new StringReader (string/join "\n" lines)))


(fact
  (read-parameters (create-reader "Foo bar" "Baz 123", "ready"))=> {"Foo" "bar", "Baz" "123"})

(fact
  (read-turn-data (create-reader "f 20 20" "w 10 10" "a 10 10 1" "d 6 6 4" "go")) =>
  (just
   {:type :food :pos {:x 20 :y 20}}
   {:type :water :pos {:x 10 :y 10}}
   {:player "1" :type :live-ant :pos {:x 10 :y 10}}
   {:player "4" :type :dead-ant :pos {:x 6 :y 6}}
    :in-any-order))
