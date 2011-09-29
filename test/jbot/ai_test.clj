(ns jbot.ai_test
  (:use 
        ai
        environment
        clojure.test
        midje.sweet))

(fact
  "If only one move available just return that"
  (preferred-moves :current-pos :history {:S :over-there})=> [:S])

(fact
  "Remove recent positions from the available dirs"
  (preferred-moves [20 10] {:positions [:here :there]} {:E :here :S :brand-new-pos}) => [ :S] 
  )

(fact
  "ignore history greater than 10 moves old"
  (preferred-moves [20 10] {:positions [:here :there :somehwere :nowhere :there :outer-space :saturn :jupiter :bangor :venice :plano]} {:E :here :S :new-place}) => [:E :S])


(fact
  "If no history don't remove recent positions"
  (preferred-moves [] {}  {:E :there :S :here}) => [:E :S])
(fact
  "If removing recent history results in no routes, remove only the most recent"
  (preferred-moves [10 10] {:positions  [:there :here :mars :chicago]} {:E :there :W :chicago } )=> [:E]) 

(fact
  "return a nil if poor ant is stuck"
  (ant-next-move {:directions {}}) => nil)

(fact
  (ant-next-move 
      {
       :history :ant-history 
       :pos [10 10] 
       :directions {:foo :bar}}) => {:pos [10 10] :direction :the-way-to-go}
  (provided
    (preferred-moves [10 10] :ant-history {:foo :bar}) => :preferred-moves
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


