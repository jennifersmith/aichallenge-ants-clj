(ns jbot.game-state_test
  (:use 
        core
        game-state
        environment
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
    (increment-state :old-env :new-data)=> :updated-env
    (increment-my-ants :old-ants :new-data) => :updated-ants))


