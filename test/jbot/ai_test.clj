(ns jbot.ai_test
  (:use 
        ai
        game-state
        clojure.test
        midje.sweet))

(def row-of-water [:water :water :water])
(def row-of-nothing [nil nil nil])
(defn random-generator [max] (rand max))


(fact
  "return a noop if poor ant is stuck"
  (ant-next-move :environment random-generator [10 10]) => {:pos [10 10] :direction nil}
  (provided
    (get-available-directions :environment [10 10])=>()))

(fact
  "Returns a random available direction using given random generator"
  (ant-next-move :environment random-generator [10 10]) => {:pos [10 10] :direction :S  }
  (provided
    (rand 2) => 1
    (get-available-directions :environment [10 10])=>[:E :S]))

(fact "Returns all the valid moves for my ants"
  (sample-bot-move {:my-ants [:a :b :c] :environment :env  :random-generator :rand})
      => [{:direction :N} {:direction :S}]
  (provided
    (ant-next-move :env :rand :c)  => {:direction nil}
    (ant-next-move :env :rand :a)  => {:direction :N}
    (ant-next-move :env :rand :b) => {:direction :S}))

