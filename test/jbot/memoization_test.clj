(ns jbot.memoization-test
  (:use memoization environment clojure.test midje.sweet))


(fact 
  "should save ant state keyed by its next position so we can look it up again"
  (save-ant-state :env {} [{:pos [10 22] :direction :N}]) =>
  {
   :next-position {:history [:N]}
   }
  (provided
    (translate-pos :env {:pos [10 22] :direction :N})=> :next-position))

(fact
  "should add previous direction history too"
  (save-ant-state :env {[10 22] {:history [:S :E :E]}} [{:pos [10 22] :direction :W}]) =>
  {
   :next-position {:history [:S :E :E :W]}}
  (provided
    (translate-pos :env {:pos [10 22] :direction :W})=> :next-position))


