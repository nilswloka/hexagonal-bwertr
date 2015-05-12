(ns hexagonal-bwertr.application.bwertr
  (:require [hexagonal-bwertr.enterprise.ratings :as ratings])
  (:import [hexagonal_bwertr.enterprise.ratings Rating]))

(defn- rate-with*! [^Rating rating ratings]
  (do
    (ratings/add! ratings rating)
    (let [average-rating (ratings/average ratings)
          number-of-ratings (ratings/count* ratings)]
      {:own-rating rating :average-rating average-rating :number-of-ratings number-of-ratings})))

(defn- rating-stats [ratings]
  (let [average-rating (ratings/average ratings)
        number-of-ratings (ratings/count* ratings)
        frequencies (ratings/stats ratings)]
    {:average-rating average-rating :number-of-ratings number-of-ratings :frequencies frequencies}))

(defprotocol BwertrApplication
  (rate-with! [this ^Rating rating])
  (statistics [this]))

(defrecord BwertrApplicationComponent [ratings]
  BwertrApplication
  (rate-with! [this rating]
    (rate-with*! rating ratings))
  (statistics [this]
    (rating-stats ratings)))

(defn new-bwertr-application []
  (map->BwertrApplicationComponent {}))











