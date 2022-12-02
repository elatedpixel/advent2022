(ns y2022.d1
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]))

;; PROBLEM LINK https://adventofcode.com/2022/day/1

;; Generator Logic

(def parser (comp
             (map str/split-lines)
             (map (partial map read-string))
             (map (partial reduce +))))

;; Solution Logic
(defn n-sum [n coll]
  (transduce (take n) + coll))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (->> (str/split input #"\n\n")
       (sequence parser)
       (sort >)))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (n-sum 1 input))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (n-sum 3 input))

; ➜ bb run :year 2022 :day 1
; Generating Input
; "Elapsed time: 16.099049 msecs"
;
; PART 1 SOLUTION:
; "Elapsed time: 0.690199 msecs"
; 70720
;
; PART 2 SOLUTION:
; "Elapsed time: 0.065624 msecs"
; 207148

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(deftest sample-test
  (t/is (= 2 (+ 1 1))))
