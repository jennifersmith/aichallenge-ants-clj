(ns jbot.world_test
  (:use 
        core
        world
        clojure.test
        midje.sweet))

(fact "create knowledge for one param"
      (increase-knowledge-of :water [{:type :ant :pos [1 2]} {:type :water :pos [4 5]} {:type :water :pos [14 5]}] [])
      => [[4 5] [14 5]])

(fact "build on top of existing knowledge"
      (increase-knowledge-of :food [{:type :food :pos [1 2]} {:type :water :pos [2 1]}] [[1 1] [5 5]])
      => [[1 1] [5 5] [1 2]])
