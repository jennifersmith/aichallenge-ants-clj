(ns jbot.ai_test
  (:use 
        ai
        environment
        clojure.test
        midje.sweet))

(def row-of-water [:water :water :water])
(def row-of-nothing [nil nil nil])
(defn random-generator [max] (rand max))

(future-fact 
  "should filter directions that would cause a 2-segment loop to repeat"
  (remove-loops [:A :B :C] [:X :C :X]) => [:A :B])

(fact
  "return a nil if poor ant is stuck"
  (ant-next-move (init-environment 10 10 ) random-generator {:history {} :pos [10 10] :directions []}) => nil)

(fact
  "Returns a random available direction using given random generator"
  (ant-next-move (init-environment  10  10)  rand {:history {} :pos [10 10] :directions [:E :S]}) => {:pos [10 10] :direction :S}
  (provided
    (rand 2) => 1))

(future-fact
  "Should not excessively loop"
  (ant-next-move (init-environment 10 10) rand {:history {:directions [:E :S :E]} :pos [10 10] :directions [:S :W]}) => (contains {:direction :W})
  (provided
    (rand 2) =streams=> [0 1]))



(fact "Passes ant pos history and available directions to compute next move"
      (move-ants {:a :history-a :b :history-b} {:my-ants {:ants [:a :b :c]} :environment :env :random-generator :rand}) =>
        [:ant-move-one :ant-move-two]
      (provided
        (get-available-directions :env :a) => :a-directions
        (get-available-directions :env :b) => :b-directions
        (get-available-directions :env :c) => :c-directions
        (ant-next-move :env :rand {:pos :a :history :history-a :directions :a-directions}) => :ant-move-one
        (ant-next-move :env :rand {:pos :b :history :history-b :directions :b-directions}) => :ant-move-two
        (ant-next-move :env :rand {:pos :c :history {} :directions :c-directions}) => nil))
