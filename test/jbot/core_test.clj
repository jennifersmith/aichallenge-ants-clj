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
  "stops at first given token"
  (read-upto "A" "B") => [["1" "2" "3"]]
  (provided (read-line) =streams=> ["1 2 3" "B" "Dont do this line"]))
;; not sure what this test is now for but it's pretty sweet after all that mocking shite one needs to do in other languages
;; so I am gonna keep it cos it's my project yeah
(fact
  (read-turn) => :turn-data-yeah
  (provided
    (parse-turn :raw-data) => :turn-data-yeah
    (read-upto "go" "end") => :raw-data))

(fact
  "should be able to render a move"
  (render-move {:pos [40 14] :direction :n})
  => "o 40 14 N")

