(ns y2022.d14
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]))

;; PROBLEM LINK https://adventofcode.com/2022/day/14

(def sample "498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9")

;; Generator Logic

(def parser
  (comp (map #(str/split % #" -> "))
        (map (partial map #(read-string (format "[%s]" %))))))

(defn- expand-path [from to]
  (let [step (mapv compare to from)]
    (take-while #(not= to %)
                (rest (iterate #(mapv + step %) from)))))

(defn- rock-coordinates [coords]
  (loop [path []
         coords coords]
    (if (empty? coords) path
        (let [current (first coords)
              prev    (peek path)]
          (recur
           (if (nil? prev)
             (conj path current)
             (conj (reduce conj path (expand-path prev current)) current))
           (rest coords))))))

(defn- rock-map [input]
  (into {}
        (map vector
             (transduce
              (comp parser (map rock-coordinates))
              concat
              (str/split-lines input))
             (repeat \#))))

;; Solution Logic

(defn- sand-fall [m blocked? [x y]]
  (some #(when ((complement (partial blocked? m)) %) %)
        [[x (inc y)] [(dec x) (inc y)] [(inc x) (inc y)]]))

(defn- simulate-sand [m bottom blocked?]
  (loop [m    m
         sand [500 0]]
    (if (or (= \* (m [500 0]))
            (<= bottom (second sand))) m
        (let [move (sand-fall m blocked? sand)]
          (cond
            (nil? move) (recur (assoc m sand \*) [500 0])
            :else (recur m move))))))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (rock-map input))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (count (filter #(= \* %) (vals (simulate-sand input
                                                (apply max (map second (keys input)))
                                                (fn [m c] (case (m c)
                                                            (\# \*) true
                                                            false)))))))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (let [bottom   (+ 2 (apply max (map second (keys input))))
        blocked? (fn [m c] (cond
                             (or
                              (#{\# \*} (m c))
                              (= bottom (second c))) true
                             :else                   false))]
    (count (filter #(= \* %) (vals (simulate-sand input bottom blocked?))))))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(deftest sample-test
  (t/is (= 24 (solve-part-1 (generator sample))))
  (t/is (= 93 (solve-part-2 (generator sample)))))
