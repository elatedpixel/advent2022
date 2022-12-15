(ns y2022.d11
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.pprint :as pprint]
            [clojure.string :as str]))

;; PROBLEM LINK https://adventofcode.com/2022/day/11

;; Generator Logic

(defn- divisible-by [n]
  (fn [x] (zero? (mod x n))))

(defrecord Monkey [id items operation test divisor inspected])

(defn make-monkey [id items operation test divisor]
  (->Monkey id items operation test divisor 0))

(defn- monkey-test [pred target-true target-false]
  (fn [n] (if (pred n) target-true target-false)))

(def re-digit #"[\d]+")
(defn- find-int [s] (parse-long (re-find re-digit s)))
(defn- find-ints [s] (map parse-long (re-seq re-digit s)))

(defn- parse-operation [operation]
  (let [[op operand] (map read-string (rest (re-find #"new = old ([+*]) (old|[\d]+)" operation)))]
    (case op
      + (fn [old] (+ old (if (number? operand) operand old)))
      * (fn [old] (* old (if (number? operand) operand old))))))

(defn parse-monkey
  [[id items operation pred true-target false-target]]
  (make-monkey (find-int id)
               (into clojure.lang.PersistentQueue/EMPTY
                     (find-ints items))
               (parse-operation operation)
               (monkey-test (divisible-by (find-int pred))
                            (find-int true-target)
                            (find-int false-target))
               (find-int pred)))

(def parser
  (comp
   (map str/split-lines)
   (map parse-monkey)))

;; Solution Logic
(defn- round [worried]
  (fn [monkeys]
    (reduce
     (fn [monkeys {:keys [id operation test]}]
       (loop [monkeys monkeys
              items   (get-in monkeys [id :items])]
         (if (empty? items) monkeys
             (let [item  (peek items)
                   worry (worried (operation item))]
               (recur (-> monkeys
                          (update-in [id :inspected] inc)
                          (update-in [id :items] pop)
                          (update-in [(test worry) :items] conj worry))
                      (pop items))))))
     monkeys
     monkeys)))

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (into [] parser (str/split input #"\n\n")))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (sort > (map :inspected (last (take 20 (rest (iterate (round #(quot % 3)) input)))))))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (let [mode (reduce * 1 (map :divisor input))]
    (sort > (map :inspected (last (take 10000 (rest (iterate (round #(mod % mode)) input))))))))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(def sample-1
  "Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1")

(deftest sample-test
  (t/is (= 2 (+ 1 1))))

(comment
  (generator sample-1)
  ;; => [{:id 0,
  ;;      :items <-(79 98)-<,
  ;;      :operation #function[y2022.d11/parse-operation/fn--21890],
  ;;      :test #function[y2022.d11/monkey-test/fn--21881],
  ;;      :inspected 0}
  ;;     {:id 1,
  ;;      :items <-(54 65 75 74)-<,
  ;;      :operation #function[y2022.d11/parse-operation/fn--21892],
  ;;      :test #function[y2022.d11/monkey-test/fn--21881],
  ;;      :inspected 0}
  ;;     {:id 2,
  ;;      :items <-(79 60 97)-<,
  ;;      :operation #function[y2022.d11/parse-operation/fn--21890],
  ;;      :test #function[y2022.d11/monkey-test/fn--21881],
  ;;      :inspected 0}
  ;;     {:id 3,
  ;;      :items <-(74)-<,
  ;;      :operation #function[y2022.d11/parse-operation/fn--21892],
  ;;      :test #function[y2022.d11/monkey-test/fn--21881],
  ;;      :inspected 0}]

  (let [input (generator sample-1)]
    (last (take 20 (rest (iterate round input)))))
  ;; => [{:id 0,
  ;;      :items <-(10 12 14 26 34)-<,
  ;;      :operation #function[y2022.d11/parse-operation/fn--8529],
  ;;      :test #function[y2022.d11/monkey-test/fn--8520],
  ;;      :inspected 101}
  ;;     {:id 1,
  ;;      :items <-(245 93 53 199 115)-<,
  ;;      :operation #function[y2022.d11/parse-operation/fn--8531],
  ;;      :test #function[y2022.d11/monkey-test/fn--8520],
  ;;      :inspected 95}
  ;;     {:id 2,
  ;;      :items <-()-<,
  ;;      :operation #function[y2022.d11/parse-operation/fn--8529],
  ;;      :test #function[y2022.d11/monkey-test/fn--8520],
  ;;      :inspected 7}
  ;;     {:id 3,
  ;;      :items <-()-<,
  ;;      :operation #function[y2022.d11/parse-operation/fn--8531],
  ;;      :test #function[y2022.d11/monkey-test/fn--8520],
  ;;      :inspected 105}]

  ;
  )
