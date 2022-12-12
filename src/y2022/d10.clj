(ns y2022.d10
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]))

;; PROBLEM LINK https://adventofcode.com/2022/day/10

(declare sample-01)

;; Generator Logic

(def parser (map #(read-string (str "(" % ")"))))

;; Solution Logic

(defn- signal-strength [[cycle register]]
  (* cycle register))

(defn- add-x [{:keys [x] :as state} n]
  (-> state
      (update :cycles conj x x)
      (update :x + n)))

(defn- nop [{:keys [x] :as state}]
  (-> state
      (update :cycles conj x)))

(def interesting-signals [20 60 100 140 180 220])

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (sequence parser (str/split-lines input)))

(defn- compute-cycles [state instruction]
  (case (first instruction)
    noop (nop state)
    addx (add-x state (second instruction))))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (transduce (map signal-strength)
             +
             (select-keys (:cycles (reduce compute-cycles {:cycles [nil] :x 1} input))
                          interesting-signals)))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (doseq [row (map (partial map list (range 40))
                   (partition 40 (rest (:cycles (reduce compute-cycles {:cycles [nil] :x 1} input)))))]
    (println (apply str (map (fn [[i x]] (if (<= (dec x) i (inc x)) "#" ".")) row)))))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

#_(deftest sample-test
  (t/is (= 13140 (solve-part-1 (generator sample-01)))))


;; Generating Input
;; "Elapsed time: 0.451104 msecs"

;; PART 1 SOLUTION:
;; "Elapsed time: 2.735813 msecs"
;; 13520

;; PART 2 SOLUTION:
;; ###...##..###..#..#.###..####..##..###..
;; #..#.#..#.#..#.#..#.#..#.#....#..#.#..#.
;; #..#.#....#..#.####.###..###..#..#.###..
;; ###..#.##.###..#..#.#..#.#....####.#..#.
;; #....#..#.#....#..#.#..#.#....#..#.#..#.
;; #.....###.#....#..#.###..####.#..#.###..
;; "Elapsed time: 1.767921 msecs"
