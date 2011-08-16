(ns jbot.world_test
  (:use 
        core
        world
        clojure.test
        midje.sweet))

(fact (init-world {:rows 20 :cols 10}) => {:player-name "0" :dimensions [20 10] :water [] :food [] :my-ants []})

(fact "pivoting a set of hashes on a key"
      (pivot :name [:foo :bar] [{:name :boris :age 99} {:name :foo :age 20} {:name :foo :age 50} {:name :bar :age 15} {:name :bar :age 19}])
      => {
          :foo [{:name :foo :age 20} {:name :foo :age 50}]
          :bar [{:name :bar :age 15} {:name :bar :age 19}]
          })

(fact "should be able to use inbound turn data to figure out the world"
      (increment-world {
                        :player-name "bob"
                        :water [[1 2] [3 4]]
                        :food [[1 2] [5 6]]
                        :my-ants [[5 1] [2 1]]
                        }
                       [
                        {:type :water :pos [15 50]}
                        {:type :food :pos [1 2]}
                        {:type :food :pos [10 10]}
                        {:type :ant :pos [5 2] :player "bob"}
                        {:type :ant :pos [2 1] :player "colin"}
                        ])
      =>
      {
       :player-name "bob"
       :water [[1 2] [3 4] [15 50]]
       :food [[1 2] [10 10]]
       :my-ants [[5 2]]
       })

(fact (get-surrounding-coords [100 100] [30 16]) =>
      [
        [[29 15] [30 15] [31 15]]
        [[29 16] [30 16] [31 16]]
        [[29 17] [30 17] [31 17]]])

(fact "can cope with wrapping" (get-surrounding-coords [100 100] [0 0])=>
    [
     [[99 99] [0 99] [1 99]]
     [[99 0] [0 0] [1 0]]
     [[99 1] [0 1] [1 1]]])

(fact "can cope with wrapping" (get-surrounding-coords [100 100] [99 99])=>
    [
     [[98 98] [99 98] [0 98]]
     [[98 99] [99 99] [0 99]]
     [[98 0] [99 0] [0 0]]])
