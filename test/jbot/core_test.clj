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
  "parsing food locations"
  (parse-turn-input "f" "20" "20") => {:type :food :pos [20 20]})
(fact
  "water..."
  (parse-turn-input "w" "15" "15") => {:type :water :pos [15 15]})
(fact
  "live ant"
  (parse-turn-input "a" "15" "15" "bob") => {:type :ant :pos [15 15] :player "bob"})
(fact 
  "dead ant (dead-ant dead-ant dead-ant dead-ant to the tune of the pink panther)"
  (parse-turn-input "d" "23" "16" "fred") => {:type :dead-ant :pos [23 16] :player "fred"})


(fact 
  (read-turn) => {:turn-number 1 :turn-data :foo}
  (provided
    (read-turn-data) => :foo
    (read-line) =streams=> ["turn 1"]))

(fact 
  "end should be signified by a nil turn"
  (read-turn)=> nil
  (provided (read-line) =streams=> ["end"]))

