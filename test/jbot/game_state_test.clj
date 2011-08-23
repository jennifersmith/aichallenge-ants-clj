(ns jbot.game-state_test
  (:use 
        core
        game-state
        clojure.test
        midje.sweet))

(defn environment-with-dimensions [rows cols]
  {:dimensions [rows cols]})

(fact (init-environment 20 10) => {:dimensions [20 10] :tiles {}})
(fact (init-my-ants "bob") => {:player-name "bob" :ants []})

(fact (init-game-state 
          {:rows "20" :cols "10" :player_seed "101"}) 
      => {
          :random-generator :new-random
          :environment :new-environment
          :my-ants :new-ants}
      (provided
        (init-environment 20 10) => :new-environment
        (init-random-generator 101) => :new-random
        (init-my-ants "0") => :new-ants))

(fact "random can be massive!" (init-game-state {:player_seed "-6519445876725383498" :rows "29" :cols "400" })=> 
      (contains [[:random-generator :foo]])
      (provided 
        (init-random-generator -6519445876725383498)=> :foo))

(fact "should be able to use inbound turn data to figure out state of the game-state"
      (:tiles (increment-environment 
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

(fact "figures out which ants are mine"
      (:ants (increment-my-ants {:player-name "bob" :ants :replace-me}  [ 
                                                                  {:type :ant :pos [20 20] :player "bob"} 
                                                                  {:type :ant :pos [14 15] :player "Henry"} 
                                                                  {:type :ant :pos [14 20] :player "bob"}]))
      => (just [[14 20] [20 20]] :in-any-order))

(fact
      (increment-game-state {:environment :old-env :my-ants :old-ants} :new-data) =>
        {
          :environment :updated-env
          :my-ants :updated-ants
         }
  (provided
    (increment-environment :old-env :new-data)=> :updated-env
    (increment-my-ants :old-ants :new-data) => :updated-ants))

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



