(ns jbot.ai_test
  (:use 
        ai
        game-state
        clojure.test
        midje.sweet))

(def row-of-water [:water :water :water])
(def row-of-nothing [nil nil nil])

(def fake-coordinates (partition 3
                                 [:NW :N :NE :W :SELF :E :SW :S :SE]))

(fact
  "when there is water everywhere, returns empty. Nowhere to run for this wee ant"
  (get-available-directions  :game-state [10 10])
    => ()
  (provided
    (game-state/get-surrounding-coords  :game-state [10 10]) => fake-coordinates
    (game-state/get-contents :game-state anything)=> :water))

(fact 
  "when there are no obstructions, you can go anywhere"
  (get-available-directions :game-state [15 15]) => (just [:N :E :S :W] :in-any-order)
  (provided
    (game-state/get-surrounding-coords  :game-state [15 15]) => fake-coordinates
    (game-state/get-contents :game-state anything) => nil))

(fact 
  "only returns directions without obstructions"
  (get-available-directions :game-state [60 60]) => (just [:E :S] :in-any-order)
  (provided
    (game-state/get-surrounding-coords :game-state [60 60]) => fake-coordinates
    (game-state/get-contents :game-state :N) => :water
    (game-state/get-contents :game-state :E) => nil
    (game-state/get-contents :game-state :S) => nil
    (game-state/get-contents :game-state :W) => :water))

(fact "only water and food is an obstruction"
      (get-available-directions :game-state [60 60]) => (just [:W :S] :in-any-order)
      (provided
        (game-state/get-surrounding-coords :game-state [60 60]) => fake-coordinates
        (game-state/get-contents :game-state :N) => :food
        (game-state/get-contents :game-state :E) => :water
        (game-state/get-contents :game-state :S) => :ant
        (game-state/get-contents :game-state :W) => :beer))
(fact
  "return a noop if poor ant is stuck"
  (ant-next-move :game-state (fn [max] 1) [10 10]) => {:pos [10 10] :direction nil}
  (provided
    (get-available-directions :game-state [10 10])=>()))

(fact
  "Returns a random available direction using given random generator"
  (ant-next-move :game-state (fn [max] 1) [10 10]) => {:pos [10 10] :direction :S  }
  (provided
    (get-available-directions :game-state [10 10])=>[:E :S]))

(fact "Returns all the valid moves for my ants"
  (sample-bot-move {:my-ants [:a :b :c] :rand-generator :rand})
      => [{:direction :N} {:direction :S}]
  (provided
    (ant-next-move {:my-ants [:a :b :c] :rand-generator :rand } :rand :c) => {:direction nil}
    (ant-next-move {:my-ants [:a :b :c] :rand-generator :rand} :rand :a) => {:direction :N}
    (ant-next-move {:my-ants [:a :b :c] :rand-generator :rand } :rand :b) => {:direction :S}))

(fact
  "init seeds the random generator"
  (sample-bot-init {:rand-seed 200}) => {:rand-seed 200 :rand-generator :seeded-rand-generator }
  (provided
    (seed-rand-generator 200) => :seeded-rand-generator))
