(ns y2022.d7
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.string :as str]
            [clojure.walk :as walk]))

;; PROBLEM LINK https://adventofcode.com/2022/day/7

;; Generator Logic

(defn- to-path [coll]
  (apply str (interpose "/" coll)))

(defn run-command [[_ op & args]]
  (fn [fs]
    (case op
      ls fs
      cd (case (str (first args))
           "/"   (assoc fs :cwd ["/"])
           ".."  (update fs :cwd pop)
           (update fs :cwd conj (str (first args)))))))

(defn add-directory [[_ dir]]
  (fn [fs] (update fs :dir assoc (to-path (conj (:cwd fs) dir)) 0)))

(defn add-file-size [[size file]]
  (fn [fs] (-> fs
               (assoc-in (conj (into [:fs] (:cwd fs)) (str file)) size)
               (update :dir (fn [f] (loop [f   f
                                           cwd (:cwd fs)]
                                      (if (empty? cwd) f
                                          (recur (update f (to-path cwd) (partial + size)) (pop cwd)))))))))

(defn cli [fs args]
  (let [arg (first args)]
    ((cond
       (= arg '$)    (run-command args)
       (= arg 'dir)  (add-directory args)
       (number? arg) (add-file-size args)
       :else         (throw (ex-info "Unknown instruction" {:text arg})))
     fs)))

;; Solution Logic

(def ^:private file-system
  {:cwd ["/"]
   :dir {"/" 0}
   :fs  {"/" {}}})

;; Entry Points

(defn generator
  "The generator fn is used to parse your input into. The output of this fn will be passed into each of the solving fns"
  [input]
  (transduce
   (map #(read-string (str "(" % ")")))
   (completing cli)
   file-system
   (str/split-lines input)))

(defn solve-part-1
  "The solution to part 1. Will be called with the result of the generator"
  [input]
  (transduce
   (filter (fn [v] (<= v 100000)))
   +
   (vals (:dir input))))

(defn solve-part-2
  "The solution to part 2. Will be called with the result of the generator"
  [input]
  (first (sort (filter (fn [v] (>= v (- 30000000 (- 70000000 (get-in input [:dir "/"])))))
                       (vals (:dir input))))))

;; Tests
;; Use tests to verify your solution. Consider using the sample data provided in the question

(def ^:private sample-input
  "$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k")

(comment
  (generator sample-input)
  ;; => {:cwd ["/" "d"],
  ;;     :dir {"/" 48381165, "//a" 94853, "//d" 24933642, "//a/e" 584},
  ;;     :fs
  ;;     {"/"
  ;;      {"b.txt" 14848514,
  ;;       "c.dat" 8504156,
  ;;       "a" {"f" 29116, "g" 2557, "h.lst" 62596, "e" {"i" 584}},
  ;;       "d" {"j" 4060174, "d.log" 8033020, "d.ext" 5626152, "k" 7214296}}}}
  )

(deftest sample-test
  (t/is (= 95437 (solve-part-1 (generator sample-input))))
  (t/is (= 24933642 (solve-part-2 (generator sample-input)))))

;; ➜ bb run :year 2022 :day 7
;; Generating Input
;; "Elapsed time: 29.852605 msecs"

;; PART 1 SOLUTION:
;; "Elapsed time: 1.915313 msecs"
;; 1118405

;; PART 2 SOLUTION:
;; "Elapsed time: 1.732852 msecs"
;; 12545514
