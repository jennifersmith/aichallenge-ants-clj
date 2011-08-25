(ns jbot.game-state_test
  (:use 
        core
        environment
        clojure.test
        midje.sweet))

(defn environment-with-dimensions [rows cols]
  {:dimensions [rows cols]})

;; contains with nestd array doesnt appear to work ? should probably write a test case for that...
(fact "should be able to use inbound turn data to figure out state of the game-state"
      (:tiles (increment-state
        {:tiles {[29 29] :water [20 10] :food }}
        [
         {:type :water :pos [15 50]}
         {:type :food :pos [1 2]}
         {:type :food :pos [10 10]}
         {:type :ant :pos [15 16] :player "ralph"}
         {:type :ant :pos [5 2] :player "bob"}
         {:type :dead-ant :pos [2 1] :player "bob"}
         {:type :dead-ant :pos [25 14] :player "ralph"}
         ]))
      => {
                           [29 29] :water 
                           [15 50] :water
                           [10 10] :food
                           [1 2]   :food
                           [15 16] :ant
                           [5 2]   :ant
                           [2 1] :dead-ant
                           [25 14] :dead-ant})

(def fake-coordinates (partition 3
                                 [:NW :N :NE :W :SELF :E :SW :S :SE]))


(fact (get-surrounding-coords (environment-with-dimensions 100 100) [30 16]) =>
      [
        [[29 15] [29 16] [29 17]]
        [[30 15] [30 16] [30 17]]
        [[31 15] [31 16] [31 17]]])

(fact "can cope with wrapping" (get-surrounding-coords (environment-with-dimensions 100 100) [0 0])=>
    [
     [[99 99] [99 0] [99 1]]
     [[0 99]  [0 0] [0 1]]
     [[1 99]  [1 0] [1 1]]])

(fact "can cope with wrapping" (get-surrounding-coords  (environment-with-dimensions 100 50) [99 49])=>
    [
     [[98 48] [98 49] [98 0]]
     [[99 48] [99 49] [99 0]]
     [[0  48] [0  49] [0 0]]])

(fact "returns what is in the square" 
    (get-contents {:tiles {[10 20] :water}} [5 5]) => nil
    (get-contents {:tiles {[10 20] :water}} [10 20]) => :water)

(fact
  "when there is water everywhere, returns empty. Nowhere to run for this wee ant"
  (get-available-directions  :env [10 10])
    => ()
  (provided
    (game-state/get-surrounding-coords :env [10 10]) => fake-coordinates
    (game-state/get-contents :env anything)=> :water))

(fact 
  "when there are no obstructions, you can go anywhere"
  (get-available-directions :env [15 15]) => (just [:N :E :S :W] :in-any-order)
  (provided
    (game-state/get-surrounding-coords  :env [15 15]) => fake-coordinates
    (game-state/get-contents :env anything) => nil))

(fact 
  "only returns directions without obstructions"
  (get-available-directions :env [60 60]) => (just [:E :S] :in-any-order)
  (provided
    (game-state/get-surrounding-coords :env [60 60]) => fake-coordinates
    (game-state/get-contents :env :N) => :water
    (game-state/get-contents :env :E) => nil
    (game-state/get-contents :env :S) => nil
    (game-state/get-contents :env :W) => :water))

(fact "only water and food is an obstruction"
      (get-available-directions :env [60 60]) => (just [:W :S] :in-any-order)
      (provided
        (game-state/get-surrounding-coords :env [60 60]) => fake-coordinates
        (game-state/get-contents :env :N) => :food
        (game-state/get-contents :env :E) => :water
        (game-state/get-contents :env :S) => :ant
        (game-state/get-contents :env :W) => :beer))



