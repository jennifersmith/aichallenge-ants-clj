(ns jbot.core_test
  (:use 
        jbot.core
        clojure.test
        midje.sweet)
  (:require [clojure.string :as string])
  (:import (java.io StringReader)))

(defn create-reader [& lines]
  (new StringReader (string/join "\n" lines)))

(defmacro bind-reader-to [& lines]
  (binding [*in* (create-reader lines)]))

(fact
  (read-parameters)=> {"Foo" "bar", "Baz" "123"}
  (against-background 
    (around :facts (bind-reader-to "Foo bar" "Baz 123", "ready"))))

(fact
  (read-turn-data ) =>
  (just
   {:type :food :pos {:x 20 :y 20}}
   {:type :water :pos {:x 10 :y 10}}
   {:player "1" :type :live-ant :pos {:x 10 :y 10}}
   {:player "4" :type :dead-ant :pos {:x 6 :y 6}}
    :in-any-order)
  (against-background
    (around :facts (bind-reader-to "f 20 20" "w 10 10" "a 10 10 1" "d 6 6 4" "go"))))
(fact 
  (read-turn (create-reader "turn 1" "f 1 2" "a 1 2 4")) => {:turn-number 1 :turn-data :foo}
  (provided
    (read-turn-data) => :foo))

(fact "end should be signified by a nil turn"
      (read-turn (create-reader "end"))=> nil)
