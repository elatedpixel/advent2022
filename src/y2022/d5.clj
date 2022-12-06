(ns y2022.d5
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]))

;; PROBLEM LINK https://adventofcode.com/2022/day/5

;; Generator Logic

(defn create-stacks [crate-stacks]
  (reduce
   (fn [m [stack & blocks]] (assoc m (Character/digit stack 10)
                                   (vec (filter #(not= \space %) blocks))))
   {}
   (apply map (comp reverse list)             ; rotates the input so columns are rows like (\1 \Z \N \space) <= this is the "top"
          (map (comp (partial map second)     ; just grab the letter or number
                     (partial partition 3 4)) ; grab the [A] bit and skip a space
               (str/split-lines crate-stacks)))))

(defn- parse-instructions [instructions]
  (map (comp vec
             (partial map parse-long)
             (partial re-seq #"[\d]+"))
       (str/split-lines instructions)))

;; Solution Logic

(defn- crate-mover-9000 [state [number-of-crates from-stack to-stack]]
  (loop [state state
         n number-of-crates]
    (if (= n 0)
      state
      (recur (-> state
                 (update from-stack pop)
                 (update to-stack conj (peek (state from-stack))))
             (dec n)))))

(defn- crate-mover-9001 [state [number-of-crates from-stack to-stack]]
  (-> state
      (update from-stack (comp vec (partial drop-last number-of-crates)))
      (update to-stack into (take-last number-of-crates (state from-stack)))))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (let [[crate-stacks instructions] (str/split input #"\n\n")]
    {:state (create-stacks crate-stacks)
     :instructions (parse-instructions instructions)}))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [{:keys [state instructions]}]
  (let [final-state (reduce
                     crate-mover-9000
                     state
                     instructions)]
    (apply str
           (for [i (range 1 (inc (count final-state)))]
             (last (final-state i))))))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [{:keys [state instructions]}]
  (let [final-state (reduce
                     crate-mover-9001
                     state
                     instructions)]
    (apply str
           (for [i (range 1 (inc (count final-state)))]
             (last (final-state i))))))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(def sample-input "    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2")

(deftest sample-test
  (t/is (= "CMZ" (solve-part-1 (generator sample-input))))
  (t/is (= "MCD" (solve-part-2 (generator sample-input)))))
