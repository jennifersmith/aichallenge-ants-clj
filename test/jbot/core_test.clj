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
