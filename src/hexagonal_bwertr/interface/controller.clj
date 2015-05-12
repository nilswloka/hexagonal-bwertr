(ns hexagonal-bwertr.interface.controller
  (:require [hexagonal-bwertr.application.bwertr :as bwertr]
            [hexagonal-bwertr.interface.view :as view]
            [hexagonal-bwertr.enterprise.ratings :as ratings]))

(defn- index-controller* [view]
  (view/index-view view))

(defn- statistics-controller* [bwertr view]
  (let [statistics (bwertr/statistics bwertr)]
    (view/statistics-view view statistics)))

(defn- rate-with-controller* [bwertr view own-rating]
  (let [rating-number (Integer/valueOf own-rating)]
    (bwertr/rate-with! bwertr (ratings/->Rating rating-number))
    (view/result-view view (assoc (bwertr/statistics bwertr) :own-rating rating-number))))

(defprotocol BwertrController
  (index-controller [this])
  (statistics-controller [this])
  (rate-with-controller [this own-rating]))

(defrecord BwertrControllerComponent [bwertr view]
  BwertrController
  (index-controller [this]
    (index-controller* view))
  (statistics-controller [this]
    (statistics-controller* bwertr view))
  (rate-with-controller [this own-rating]
    (rate-with-controller* bwertr view own-rating)))

(defn new-bwertr-controller []
  (map->BwertrControllerComponent {}))



