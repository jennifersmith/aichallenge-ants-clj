(ns jbot.ai_test
  (:use 
        ai
        environment
        clojure.test
        midje.sweet))

(fact
  "should filter directions that would cause a 2-segment loop to repeat"
  (remove-loops [:Y :Y :Y :X :C :X] [:A :B :C]) => [:A :B])

(fact 
  "dont do anything if moves history too short"
  (remove-loops [:C :X] [:A :B :C]) => [:A :B :C]
  (remove-loops [:X] [:A :B :C]) => [:A :B :C])

(fact 
  "return everything if no loops"
  (remove-loops [:X :Y] [:A :B :C]) => [:A :B :C])

(fact
  "If only one move available just return that"
  (preferred-moves :history [:over-there])=> [:over-there])

(fact
  "Remove potential overlooped paths from the available dirs"
  (preferred-moves {:directions [:N :S :E]} [:E :S]) => :loops-removed
  (provided
    (remove-loops [:N :S :E] [:E :S])=> :loops-removed))

(fact
  "If no history don't remove loops"
  (preferred-moves {} [:E :S]) => [:E :S])

(fact
  "return a nil if poor ant is stuck"
  (ant-next-move {:directions []}) => nil)

(fact
  (ant-next-move 
      {
       :history :ant-history 
       :pos [10 10] 
       :directions [:Here :There] }) => {:pos [10 10] :direction :the-way-to-go}
  (provided
    (preferred-moves :ant-history [:Here :There]) => :preferred-moves
    (rand-nth :preferred-moves) => :the-way-to-go))

(fact "Passes ant pos history and available directions to compute next move"
      (move-ants {:a :history-a :b :history-b} {:my-ants {:ants [:a :b :c]} :environment :env :random-generator :rand}) =>
        [:ant-move-one :ant-move-two]
      (provided
        (get-available-directions :env :a) => :a-directions
        (get-available-directions :env :b) => :b-directions
        (get-available-directions :env :c) => :c-directions
        (ant-next-move {:pos :a :history :history-a :directions :a-directions}) => :ant-move-one
        (ant-next-move {:pos :b :history :history-b :directions :b-directions}) => :ant-move-two
        (ant-next-move {:pos :c :history {} :directions :c-directions}) => nil))
