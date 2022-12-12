(ns y2022.d9
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]))

;; PROBLEM LINK https://adventofcode.com/2022/day/9

(def sample-input
  "R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2")

(def sample-input-2
  "R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20")

;; Generator Logic

(def parser (map (comp read-string #(str "(" % ")"))))

;; Solution Logic

(defn- make-rope [n]
  {:rope    (vec (repeat n [0 0]))
   :visited #{[0 0]}})

(defn- distance [[y1 x1] [y2 x2]]
  [(abs (- y1 y2)) (abs (- x1 x2))])

(defn- offset [t h]
  (if (< t h) (inc t) (dec t)))

(defn- update-tail [[y1 x1] [y2 x2]]
  (let [d (distance [y1 x1] [y2 x2])]
    (case d
      ([0 0] [0 1] [1 0] [1 1]) [y1 x1]
      [2 0] [(offset y1 y2) x1]
      [0 2] [y1 (offset x1 x2)]
      ([1 2] [2 1] [2 2]) [(offset y1 y2) (offset x1 x2)])))

(defn move [state [dir n]]
  (loop [i                             n
         {:keys [rope] :as state} state]
    (if (zero? i) state
        (let [rope' (loop [j 0
                           rope rope
                           prev nil]
                      (if ((complement contains?) rope j) rope
                          (let [prev' (if (nil? prev)
                                        (case dir
                                          L (update (rope j) 1 dec)
                                          R (update (rope j) 1 inc)
                                          U (update (rope j) 0 inc)
                                          D (update (rope j) 0 dec))
                                        (update-tail (rope j) prev))]
                            (recur (inc j)
                                   (assoc rope j prev')
                                   prev'))))]
          (recur (dec i)
                 (-> state
                     (assoc :rope rope')
                     (update :visited conj (last rope'))))))))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (sequence parser (str/split-lines input)))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (count (:visited (reduce move (make-rope 2) input))))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (count (:visited (reduce move (make-rope 9) input))))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(deftest sample-test
  (t/is (= 13 (solve-part-1 (generator sample-input))))
  (t/is (= 36 (solve-part-2 (generator sample-input-2)))))
