(ns y2022.d6
  (:require [clojure.test :as t :refer [deftest]]))

;; PROBLEM LINK https://adventofcode.com/2022/day/6

(defn- index-of-n-distinct-characters [n input]
  (loop [i n]
    (if (= n (count (set (subs input (- i n) i))))
      i
      (recur (inc i)))))

(defn- start-of-packet-marker [input]
  (index-of-n-distinct-characters 4 input))

(defn- start-of-message-marker [input]
  (index-of-n-distinct-characters 14 input))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input] input)

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input] (start-of-packet-marker input))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input] (start-of-message-marker input))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(deftest sample-test

  (t/are [input marker]
         (= marker (solve-part-1 (generator input)))
    "mjqjpqmgbljsphdztnvjfqwrcgsmlb" 7
    "bvwbjplbgvbhsrlpgdmjqwftvncz" 5
    "nppdvjthqldpwncqszvftbrmjlhg" 6
    "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" 10
    "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" 11)

  (t/are [input marker]
         (= marker (solve-part-2 (generator input)))
    "mjqjpqmgbljsphdztnvjfqwrcgsmlb" 19
    "bvwbjplbgvbhsrlpgdmjqwftvncz" 23
    "nppdvjthqldpwncqszvftbrmjlhg" 23
    "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" 29
    "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" 26))

;; ➜ bb run :year 2022 :day 6
;; Generating Input
;; "Elapsed time: 0.006034 msecs"

;; PART 1 SOLUTION:
;; "Elapsed time: 4.85923 msecs"
;; 1080

;; PART 2 SOLUTION:
;; "Elapsed time: 13.827883 msecs"
;; 3645
