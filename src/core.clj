(ns core)

(defn invert-map [m]
  (into {} (for [[k v] m] [v k])))
