(ns y2022.d2
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]
            [clojure.walk :as walk]))

;; PROBLEM LINK https://adventofcode.com/2022/day/2

(def shapes (map str '(rock paper scissors)))
(def outcomes (map str '(lose draw win)))

;; Generator Logic

(def opponent-map (zipmap (map str "ABC") shapes))
(def strategy-map (zipmap (map str "XYZ") shapes))
(def goal-map (zipmap (map str "XYZ") outcomes))

(defn parser [replacements]
  (comp (map #(str/replace % #"[A-Z]" replacements))
        (map #(read-string (str "(" % ")")))))

;; Solution Logic

(def rules
  {:rock     {:paper    :win
              :rock     :draw
              :scissors :lose}
   :paper    {:paper    :draw
              :rock     :lose
              :scissors :win}
   :scissors {:paper    :lose
              :rock     :win
              :scissors :draw}})

(def shape-score
  {:rock     1
   :paper    2
   :scissors 3})

(def outcome-score
  {:lose 0
   :draw 3
   :win  6})

(defn score [& things]
  (transduce (map (merge shape-score outcome-score)) + things))

(defn shoot [rules round]
  (score
   (keyword (second round))
   (get-in rules (mapv keyword round))))

(defn play [rules decoder rounds]
  (transduce (map (partial shoot rules)) +
             (sequence (parser decoder) rounds)))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (str/split-lines input))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (play rules (merge opponent-map strategy-map) input))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (play (reduce-kv (fn [m k v] (assoc m k (into {} (for [[k' v'] v] [v' k'])))) {} rules)
        (merge opponent-map goal-map) input))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(def ^:private sample-input
"A Y
B X
C Z")

(deftest sample-test
  (t/is (= 15 (solve-part-1 (generator sample-input))))
  (t/is (= 12 (solve-part-2 (generator sample-input)))))

;; ➜ bb run :day 2
;; Generating Input
;; "Elapsed time: 3.588742 msecs"

;; PART 1 SOLUTION:
;; "Elapsed time: 75.452145 msecs"
;; 10624

;; PART 2 SOLUTION:
;; "Elapsed time: 20.340148 msecs"
;; 14060
