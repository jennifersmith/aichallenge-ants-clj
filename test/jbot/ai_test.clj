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
    (world/get-surrounding-coords-1  :world [10 10]) => fake-coordinates
    (world/get-contents :world anything)=> :water))

(fact 
  "when there are no obstructions, you can go anywhere"
  (get-available-directions :world [15 15]) => (just [:N :E :S :W] :in-any-order)
  (provided
    (world/get-surrounding-coords-1  :world [15 15]) => fake-coordinates
    (world/get-contents :world anything) => nil))

(fact 
  "only returns directions without obstructions"
  (get-available-directions :world [60 60]) => (just [:E :S] :in-any-order)
  (provided
    (world/get-surrounding-coords-1 :world [60 60]) => fake-coordinates
    (world/get-contents :world :N) => :water
    (world/get-contents :world :E) => nil
    (world/get-contents :world :S) => nil
    (world/get-contents :world :W) => :water))

