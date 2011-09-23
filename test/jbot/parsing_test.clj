(ns jbot.parsing_test
  (:use 
        parsing
        clojure.test
        midje.sweet))

(tabular
  (fact 
    "parsing food locations"
    (parse-turn-input ?type ?row ?col) => (contains {:type ?expected-type :pos ?expected-pos}))
    ?type   ?row    ?col    ?expected-type    ?expected-pos
    "f"     "20"    "20"    :food             [20 20]     
    "w"     "15"    "15"    :water            [15 15]
    "a"     "66"    "77"    :ant              [66 77]
    "d"     "15"    "101"   :dead-ant         [15 101]
  )
(fact
  "ants have names - dead ones and live ones"
  (parse-turn-input "a" "15" "15" "bob") => (contains {:player "bob"})
  (parse-turn-input "d" "15" "15" "gary") => (contains {:player "gary"}))

(fact 
  (parse-turn [["turn" "1"] ["data1"] ["data2"] ["go"]]) => {:turn-number 1 :turn-data [:one :two]}
  (provided
    (parse-turn-input "data1") => :one
    (parse-turn-input "data2") => :two))

(fact 
  "WTF"
  (parse-turn ['("turn" "35") '("a" "24" "61" "0")]) => {:turn-number 1 :turn-data [{:type :ant :pos [24 61] :player "0"}]})

(fact 
  "end should be signified by a nil turn"
  (parse-turn [["end"]])=> nil)

(fact
  (parse-parameters [["player_seed" "22"] ["rows" "100"]  ["cols" "10"]])=> {:player_seed 22 :rows 100 :cols 10})

(fact "random can be massive!" 
      (parse-parameters [["player_seed" "-6519445876725383498"] ["rows" "29"] ["cols" "400"]]) => 
      (contains [[:player_seed -6519445876725383498]]))
