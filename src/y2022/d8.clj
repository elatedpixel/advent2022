(ns y2022.d8
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]))

;; PROBLEM LINK https://adventofcode.com/2022/day/8

;; Generator Logic

;; Solution Logic

(defn- max-heights-from-outside [tree-heights]
  (let [rows          (count tree-heights)
        cols          (count (tree-heights 0))
        visible-trees (atom {})
        max-west      (atom (vec (repeat rows -1)))
        max-north     (atom (vec (repeat cols -1)))
        max-east      (atom (vec (repeat rows -1)))
        max-south     (atom (vec (repeat cols -1)))]
    ;; start at north-west
    (doseq [row (range rows)
            col (range cols)]
      (let [height  (get-in tree-heights [row col])]
        (swap! visible-trees update [row col] (fnil concat []) [height (@max-west row) (@max-north col)])
        (swap! max-west update row max height)
        (swap! max-north update col max height)))
    ;; start at south-east
    (doseq [row (reverse (range rows))
            col (reverse (range cols))]
      (let [height  (get-in tree-heights [row col])]
        (swap! visible-trees update [row col] (fnil concat []) [height (@max-east row) (@max-south col)])
        (swap! max-east update row max height)
        (swap! max-south update col max height)))
    @visible-trees))

(defn- visible-from-outside [max-heights]
  (count (filter (fn [[_ [h & outside]]] (some (partial > h) outside)) max-heights)))

(defn- tree-view [m row col]
  (let [h (get-in m [row col])
        n (count m)
        u (loop [x 0 i row]
            (cond
              (<= i 0)                        x
              (<= h (get-in m [(dec i) col])) (inc x)
              :else                           (recur (inc x) (dec i))))
        d (loop [x 0 i row]
            (cond
              (<= (dec n) i)                        x
              (<= h (get-in m [(inc i) col])) (inc x)
              :else                           (recur (inc x) (inc i))))
        l (loop [x 0 i col]
            (cond
              (<= i 0)                        x
              (<= h (get-in m [row (dec i)])) (inc x)
              :else                           (recur (inc x) (dec i))))
        r (loop [x 0 i col]
            (cond
              (<= (dec n) i)                        x
              (<= h (get-in m [row (inc i)])) (inc x)
              :else                           (recur (inc x) (inc i))))]
    (* u d l r)))

(defn- trees-viewable-from-tree [tree-heights]
  (let [rows          (count tree-heights)
        cols          (count (tree-heights 0))
        visible-trees (atom {})]
    (doseq [row (range rows)
            col (range cols)]
      (swap! visible-trees assoc [row col] (tree-view tree-heights row col)))
    @visible-trees))

(defn- scenic-score [trees-visible]
  (apply max (vals trees-visible)))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (mapv (partial mapv #(Character/digit % 10))
        (str/split-lines input)))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (visible-from-outside (max-heights-from-outside input)))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (scenic-score (trees-viewable-from-tree input)))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question
(def sample-input
  "30373
25512
65332
33549
35390")

(deftest sample-test
  (t/is (= 21 (solve-part-1 (generator sample-input))))
  (t/is (= 8 (solve-part-2 (generator sample-input)))))

;; ➜ bb run :year 2022 :day 8
;; Generating Input
;; "Elapsed time: 106.38487 msecs"

;; PART 1 SOLUTION:
;; "Elapsed time: 134.770559 msecs"
;; 1829

;; PART 2 SOLUTION:
;; "Elapsed time: 60.131789 msecs"
;; 291840
