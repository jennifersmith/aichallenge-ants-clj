(ns structure)

;; Sticking some shared protocols in here... not sure this is a good idea but its the best I have right now

(defprotocol IncrementableState
  "Stuff that changes turn by turn"
  (increment-state [current-state new-information]))

