(ns jbot.ai_test
  (:use 
        ai
        environment
        clojure.test
        midje.sweet))

(def row-of-water [:water :water :water])
(def row-of-nothing [nil nil nil])
(defn random-generator [max] (rand max))


(fact
  "return a nil if poor ant is stuck"
  (ant-next-move random-generator {:pos [10 10] :directions []}) => nil)

(fact
  "Returns a random available direction using given random generator"
  (ant-next-move rand {:pos [10 10] :directions [:E :S]}) => {:pos [10 10] :direction :S}
  (provided
    (rand 2) => 1))


(fact "Passes ant pos and available directions to compute next move"
      (move-ants {:my-ants {:ants [:a :b :c]} :environment :env :random-generator :rand}) =>
        [:ant-move-one :ant-move-two]
      (provided
        (get-available-directions :env :a) => :a-directions
        (get-available-directions :env :b) => :b-directions
        (get-available-directions :env :c) => :c-directions
        (ant-next-move :rand {:pos :a :directions :a-directions}) => :ant-move-one
        (ant-next-move :rand {:pos :b :directions :b-directions}) => :ant-move-two
        (ant-next-move :rand {:pos :c :directions :c-directions}) => nil))
