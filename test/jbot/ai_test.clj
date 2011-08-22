(ns jbot.ai_test
  (:use 
        ai
        world
        clojure.test
        midje.sweet))

(def row-of-water [:water :water :water])
(def row-of-nothing [nil nil nil])

(def fake-coordinates (partition 3
                                 [:NW :N :NE :W :SELF :E :SW :S :SE]))

(fact
  "when there is water everywhere, returns empty. Nowhere to run for this wee ant"
  (get-available-directions  :world [10 10])
    => ()
  (provided
    (world/get-surrounding-coords  :world [10 10]) => fake-coordinates
    (world/get-contents :world anything)=> :water))

(fact 
  "when there are no obstructions, you can go anywhere"
  (get-available-directions :world [15 15]) => (just [:N :E :S :W] :in-any-order)
  (provided
    (world/get-surrounding-coords  :world [15 15]) => fake-coordinates
    (world/get-contents :world anything) => nil))

(fact 
  "only returns directions without obstructions"
  (get-available-directions :world [60 60]) => (just [:E :S] :in-any-order)
  (provided
    (world/get-surrounding-coords :world [60 60]) => fake-coordinates
    (world/get-contents :world :N) => :water
    (world/get-contents :world :E) => nil
    (world/get-contents :world :S) => nil
    (world/get-contents :world :W) => :water))

(fact "only water and food is an obstruction"
      (get-available-directions :world [60 60]) => (just [:W :S] :in-any-order)
      (provided
        (world/get-surrounding-coords :world [60 60]) => fake-coordinates
        (world/get-contents :world :N) => :food
        (world/get-contents :world :E) => :water
        (world/get-contents :world :S) => :ant
        (world/get-contents :world :W) => :beer))
(fact
  "return a noop if poor ant is stuck"
  (ant-next-move :world (fn [max] 1) [10 10]) => {:pos [10 10] :direction nil}
  (provided
    (get-available-directions :world [10 10])=>()))

(fact
  "Returns a random available direction using given random generator"
  (ant-next-move :world (fn [max] 1) [10 10]) => {:pos [10 10] :direction :S  }
  (provided
    (get-available-directions :world [10 10])=>[:E :S]))

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
