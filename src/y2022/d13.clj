(ns y2022.d13
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]
            [clojure.walk :as walk]))

;; PROBLEM LINK https://adventofcode.com/2022/day/13

(def sample
  "[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]")

;; Generator Logic

(def parser
  (comp (map str/split-lines)
        (map (partial map read-string))))

;; Solution Logic

(defn- compare* [left right]
  (cond
    (and (coll? left) (coll? right))
    (loop [[l & left] left
           [r & right] right]
      (if (every? nil? (list l r))
        0
        (let [result (compare* l r)]
          (cond ((complement zero?) result) result
                :else (recur left right)))))

    (coll? left)
    (compare* left (list right))

    (coll? right)
    (compare* (list left) right)

    :else (compare left right)))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  input)

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (transduce
   (comp
    (filter (comp neg? second))
    (map (comp inc first)))
   +
   (map-indexed (fn [i [l r]] [i (compare* l r)])
                (sequence parser (str/split input #"\n\n")))))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (apply *
         (let [p2 (sort compare* (conj (map read-string (str/split input #"\n+"))
                                       [[2]]
                                       [[6]]))]
           (for [i (range (count p2))
                 :when (#{[[2]] [[6]]} (nth p2 i))]
             (inc i)))))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(deftest sample-test
  (t/is (= 13 (solve-part-1 (generator sample))))
  (t/is (= 140 (solve-part-2 (generator sample)))))
