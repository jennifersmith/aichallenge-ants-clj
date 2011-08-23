(ns jbot.ai_test
  (:use 
        ai
        game-state
        clojure.test
        midje.sweet))

(def row-of-water [:water :water :water])
(def row-of-nothing [nil nil nil])

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

