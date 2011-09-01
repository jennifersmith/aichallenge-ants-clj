(ns parsing (:use debug))
(defn parse-int [value] (Integer/parseInt value)) ;; I dont think can use a java function as a clojure fn right in map and stuff like that?

;; Should I maybe assert "turn" as in "turn 2" and "end" as in the end of the game?

(def code->event-type 
  {"a" :ant
   "d" :dead-ant
   "f" :food
   "w" :water})

(defn read-turn-header
  ([turn-message turn-number] (Integer/parseInt turn-number))
  ([end-message] nil)
  ([too many & args] (throw (Exception. (apply str (flatten (cons ["invalid call to turn header" too many] args)))))))


(defn parse-turn-input 
  ([event-type x y player] (assoc (parse-turn-input event-type x y) :player player))
  ([event-type x y] {:type (code->event-type event-type) :pos (vec (map parse-int  [x y]))}))

(defn parse-turn [[turn-header-line & remaining-lines]]
  (if-let [turn-header (apply read-turn-header turn-header-line)]
    (let [turn-data-lines (butlast remaining-lines)]
      {
       :turn-number turn-header
        :turn-data (seq (map (partial apply parse-turn-input) turn-data-lines))})))

(defn parse-parameters [lines]
  (let
    [raw-params
      (reduce (fn [result [k v]] 
           (assoc result (keyword k) v))
           {}           
            lines)]
    {
     :rows (Integer/parseInt (:rows raw-params))
     :cols (Integer/parseInt (:cols raw-params))
     :player_seed (Long/parseLong (:player_seed raw-params))
     }))
