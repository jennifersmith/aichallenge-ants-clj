(ns jbot.core_test
  (:use 
        core
        parsing
        clojure.test
        midje.sweet)
  (:require [clojure.string :as string]))
(fact
  (read-parameters)=> {:Foo "bar" :Baz "123"}
  (provided (read-line) =streams=> ["Foo bar" "Baz 123" "ready"]))
(fact
  "read-one reads and tokenizes just one line"
  (read-one) => ["A" "B"]
  (provided (read-line) => "A B"))

(fact
  "read-upto reads till a given stop-token and tokenises the lines"
  (read-upto "foo") => [["A" "B" "C"]["D" "E" "F"]]
  (provided (read-line) =streams=> ["A B C" "D E F" "foo"]))

(fact 
  (read-turn) => {:turn-number 1 :turn-data [:one :two]}
  (provided
    (parse-turn-input "data1") => :one
    (parse-turn-input "data2") => :two
    (read-line) =streams=> ["turn 1" "data1" "data2" "go"]))

(fact 
  "end should be signified by a nil turn"
  (read-turn)=> nil
  (provided (read-line) =streams=> ["end"]))

(fact
  "should be able to render a move"
  (render-move {:pos [40 14] :direction :n})
  => "o 40 14 N")

