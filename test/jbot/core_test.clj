(ns jbot.core_test
  (:use 
        jbot.core
        clojure.test
        midje.sweet)
  (:require [clojure.string :as string]))

(fact
  (read-parameters)=> {"Foo" "bar", "Baz" "123"}
  (provided (read-line) =streams=> ["Foo bar" "Baz 123" "ready"]))

(fact
  (read-turn-data ) =>
  (just
   {:type :food :pos {:x 20 :y 20}}
   {:type :water :pos {:x 10 :y 10}}
   {:player "1" :type :live-ant :pos {:x 10 :y 10}}
   {:player "4" :type :dead-ant :pos {:x 6 :y 6}}
    :in-any-order)
  (provided (read-line) =streams=> ["f 20 20" "w 10 10" "a 10 10 1" "d 6 6 4" "go"]))
(fact 
  (read-turn) => {:turn-number 1 :turn-data :foo}
  (provided
    (read-turn-data) => :foo
    (read-line) =streams=> ["turn 1"]))

(fact 
  "end should be signified by a nil turn"
  (read-turn)=> nil
  (provided (read-line) =streams=> ["end"]))

