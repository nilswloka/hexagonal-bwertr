(ns hexagonal-bwertr.enterprise.schemas
  (:require [schema.core :as s]))

;;;; generic helpers ;;;;;;;;;

(defn upper-bound-number-pred [max-val]
  (s/pred (fn [n]
            (<= n max-val))
          (str "upper bound number, max: " max-val)))

(defn upper-bound-number [max-val]
  (s/both Integer
          (upper-bound-number-pred max-val)))

(defn lower-bound-number-pred [min-val]
  (s/pred (fn [n]
            (<= min-val n))
          (str "lower bound number, min: " min-val)))

(defn lower-bound-number [min-val]
  (s/both Integer
          (lower-bound-number-pred min-val)))

(defn lower-upper-bound-number [lower upper]
  (s/both Integer
          (lower-bound-number-pred lower)
          (upper-bound-number-pred upper)))

(defn validation-error? [exdat]
  (-> exdat (:type) (= :schema.core/error)))