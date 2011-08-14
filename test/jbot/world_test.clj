(ns jbot.world_test
  (:use 
        core
        world
        clojure.test
        midje.sweet))

(fact (init-world) => {:player-name "0" :water [] :food [] :my-ants []})

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

