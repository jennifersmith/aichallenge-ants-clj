(ns jbot.environment_test
  (:use 
        core
        environment
        structure
        clojure.test
        midje.sweet))

(defn environment-with-dimensions [rows cols]
  (init-environment rows cols) 
  )

(defn environment-with-tiles [tiles]
  (assoc
    (init-environment 100 100) :tiles tiles)
  )

(tabular
  (fact
    (translate-pos (environment-with-dimensions 100 100) {:pos [?start-row ?start-col] :direction ?direction}) => [?expected-row ?expected-col])
    ?start-row  ?start-col  ?direction  ?expected-row ?expected-col
    22           14            :N           21           14
    22           14            :E           22           15
    22           14            :S           23           14
    22           14            :W           22           13
    0             0            :N           99            0
    0             0            :W           0             99
    99            0            :S           0             0
    99            99           :E           99            0)


;; contains with nestd array doesnt appear to work ? should probably write a test case for that...
(fact "should be able to use inbound turn data to figure out state of the game-state"
     (:tiles (increment-state
       (environment-with-tiles 
        {[29 29] :water [20 10] :food }) 
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
    (get-contents (environment-with-tiles {[10 20] :water}) [5 5]) => nil
    (get-contents (environment-with-tiles {[10 20] :water}) [10 20]) => :water)

(fact
  "when there is water everywhere, returns empty. Nowhere to run for this wee ant"
  (get-available-directions (environment-with-tiles {}) [10 10])
    => ()
  (provided
    (get-surrounding-coords anything [10 10]) => fake-coordinates
     (get-contents anything anything)=> :water))

(fact 
  "when there are no obstructions, you can go anywhere"
  (get-available-directions (environment-with-tiles []) [15 15]) => {:N [14 15] :E [15 16] :S [16 15]  :W [15 14] }
  (provided
    (get-contents anything anything) => nil))

(fact 
  "only returns directions without obstructions"
  (get-available-directions :env [60 60]) => {:E [60 61] :S [61 60] }
  (provided
    (get-surrounding-coords :env [60 60]) => fake-coordinates
    (get-contents :env :N) => :water
    (get-contents :env :E) => nil
    (get-contents :env :S) => nil
    (get-contents :env :W) => :water))

(fact "only water and food is an obstruction"
      (get-available-directions :env [60 60]) => { :W [60 59] :S [61 60]}
      (provided
        (get-surrounding-coords :env [60 60]) => fake-coordinates
        (get-contents :env :N) => :food
        (get-contents :env :E) => :water
        (get-contents :env :S) => :ant
        (get-contents :env :W) => :beer))



