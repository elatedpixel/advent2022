(ns y2022.d4
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]))

;; PROBLEM LINK https://adventofcode.com/2022/day/4

;; Generator Logic

;; Solution Logic

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (sequence
   (comp (map (partial re-seq #"[\d]+"))
         (map (partial map parse-long))
         (map (partial partition 2)))
   (str/split-lines input)))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (count (filter (fn [[[a b] [c d]]] (or (<= a c d b)
                                         (<= c a b d)))
                 input)))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (count (filter (fn [[[a b] [c d]]]
                   (or (<= a d b)
                       (<= c b d)))
                 input)))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(def sample-input "2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8")

(deftest sample-test
  (t/is (= 2 (solve-part-1 (generator sample-input))))
  (t/is (= 4 (solve-part-2 (generator sample-input)))))

;; ➜ bb run :year 2022 :day 4
;; Generating Input
;; "Elapsed time: 4.772619 msecs"

;; PART 1 SOLUTION:
;; "Elapsed time: 11.514979 msecs"
;; 471

;; PART 2 SOLUTION:
;; "Elapsed time: 7.625092 msecs"
;; 888
