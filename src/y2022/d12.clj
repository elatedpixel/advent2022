(ns y2022.d12
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]))

;; PROBLEM LINK https://adventofcode.com/2022/day/12

(def sample-1
  "Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi")

;; Generator Logic

;; Solution Logic

(defn- elevation [x]
  (case x
    \S \a
    \E \z
    x))

(defn- valid-move? [from to]
  (<= (compare (elevation to)
               (elevation from)) 1))

(defn- neighbors [m [y x]]
  (sequence
   (comp (map (partial map + [y x]))
         (map vec)
         (filter #(and (some? (m %))
                       (valid-move? (m [y x]) (m %)))))
   (list [-1 0] [1 0] [0 -1] [0 1])))

(defn graph-search [graph start goal?]
  (loop [unexplored (conj clojure.lang.PersistentQueue/EMPTY [start []])
         visited #{}]
    (cond
      (empty? unexplored) 999999
      (goal? (first (peek unexplored))) (count (second (peek unexplored)))
      (visited (first (peek unexplored))) (recur (pop unexplored) visited)
      :else (let [[current path] (peek unexplored)]
              (recur (into (pop unexplored) (map (juxt vec (partial conj path)) (graph current)))
                     (conj visited current))))))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (let [m     (into [] (map vec) (str/split-lines input))
        r     (count m)
        c     (count (first m))
        hm    (into {} (for [i (range r)
                             j (range c)]
                         [[i j] (get-in m [i j])]))
        g     (into {} (for [i (range r)
                             j (range c)]
                         [[i j] (neighbors hm [i j])]))
        start (some (fn [[k v]] (when (= v \S) k)) hm)]
    {:start     start
     :graph     g
     :heightmap hm}))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [{:keys [start graph heightmap]}]
  (graph-search graph start (fn [coord] (= \E (heightmap coord)))))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [{:keys [graph heightmap]}]
  (apply min
         (keep (fn [[k v]] (when (#{\S \a} v)
                             (solve-part-1 {:start k :graph graph :heightmap heightmap})))
               heightmap)))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(deftest sample-test
  (t/is (= 31 (solve-part-1 (generator sample-1)))))


(solve-part-2 (generator (slurp "input/2022/12.txt")))
