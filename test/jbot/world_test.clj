(ns jbot.world_test
  (:use 
        core
        world
        clojure.test
        midje.sweet))

(defn world-with-dimensions [rows cols]
  {:dimensions [rows cols]})

(fact (init-world {:rows "20" :cols "10"}) => {:player-name "0" :dimensions [20 10] :environment {} :my-ants[] })

(fact "pivoting a set of hashes on a key"
      (pivot :name [:foo :bar] [{:name :boris :age 99} {:name :foo :age 20} {:name :foo :age 50} {:name :bar :age 15} {:name :bar :age 19}])
      => {
          :foo [{:name :foo :age 20} {:name :foo :age 50}]
          :bar [{:name :bar :age 15} {:name :bar :age 19}]
          })

(fact "should be able to use inbound turn data to figure out state of the world"
      (:environment (increment-world {}
                                     [
                                      {:type :water :pos [15 50]}
                                      {:type :food :pos [1 2]}
                                      {:type :food :pos [10 10]}
                                      {:type :ant :pos [15 16] :player "ralph"}
                                      {:type :ant :pos [5 2] :player "bob"}
                                      {:type :dead-ant :pos [2 1] :player "bob"}
                                      {:type :dead-ant :pos [25 14] :player "ralph"}
                                      ]))
      =>{
               [15 50] :water
               [10 10] :food
               [1 2]   :food
               [15 16] :ant
               [5 2]   :ant
               [2 1] :dead-ant
               [25 14] :dead-ant})

(fact "figures out which ants are mine"
      (:my-ants (increment-world {:player-name "bob"} [{:type :ant :pos [20 20] :player "bob"} {:type :ant :pos [14 15] :player "Henry"} {:type :ant :pos [14 20] :player "bob"}]))
      => (just [14 20] [20 20] :in-any-order))

(fact (get-surrounding-coords (world-with-dimensions 100 100) [30 16]) =>
      [
        [[29 15] [29 16] [29 17]]
        [[30 15] [30 16] [30 17]]
        [[31 15] [31 16] [31 17]]])

(fact "can cope with wrapping" (get-surrounding-coords (world-with-dimensions 100 100) [0 0])=>
    [
     [[99 99] [99 0] [99 1]]
     [[0 99]  [0 0] [0 1]]
     [[1 99]  [1 0] [1 1]]])

(fact "can cope with wrapping" (get-surrounding-coords  (world-with-dimensions 100 50) [99 49])=>
    [
     [[98 48] [98 49] [98 0]]
     [[99 48] [99 49] [99 0]]
     [[0  48] [0  49] [0 0]]])

(fact "returns what is in the square" 
    (get-contents {:environment {[10 20] :water}} [5 5]) => nil
    (get-contents {:environment {[10 20] :water}} [10 20]) => :water)



