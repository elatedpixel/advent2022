(ns y2022.d3
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]
            [clojure.set :as set]))

;; PROBLEM LINK https://adventofcode.com/2022/day/3

;; Generator Logic

(def tall-letters (reduce str (map char (range 65 (+ 65 26)))))
(def small-letters (reduce str (map char (range 97 (+ 97 26)))))

(def priorities
  (merge (zipmap small-letters (range 1 27))
         (zipmap tall-letters (range 27 53))))

(def parser
  (map (partial map priorities)))

;; Solution Logic

(defn- split-in-half
  [coll]
  (split-at (bit-shift-right (count coll) 1) coll))

(defn- common-priorities [pairs]
  (reduce + (for [[a b] pairs]
              (reduce + (set/intersection (set a) (set b))))))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (sequence parser (str/split-lines input)))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (common-priorities (map split-in-half input)))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (reduce
   +
   (transduce
    (map (fn [group] (apply set/intersection (map set group))))
    concat
    (partition 3 input))))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(def sample-input
  "vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw")

(deftest sample-test
  (t/is (= 157 (solve-part-1 (generator sample-input))))
  (t/is (= 70 (solve-part-2 (generator sample-input)))))

;; ➜ bb run :year 2022 :day 3
;; Generating Input
;; "Elapsed time: 4.348484 msecs"

;; PART 1 SOLUTION:
;; "Elapsed time: 12.369296 msecs"
;; 8349

;; PART 2 SOLUTION:
;; "Elapsed time: 19.56682 msecs"
;; 2681
