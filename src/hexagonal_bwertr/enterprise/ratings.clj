(ns hexagonal-bwertr.enterprise.ratings)

(defrecord Rating [value])

(defprotocol RatingsRepository
  (store! [this ^Rating rating])
  (retrieve-all [this]))

(defprotocol Ratings
  (add! [this ^Rating rating])
  (count* [this])
  (stats [this])
  (average [this]))

(defn- add-rating! [^Rating rating repository]
  (store! repository rating))

(defn- count-ratings [repository]
  (count (retrieve-all repository)))

(defn- rating-stats [repository]
  (let [all-ratings (retrieve-all repository)
        all-values (map :value all-ratings)]
    (frequencies all-values)))

(defn- average-rating [repository]
  (when-let [all-ratings (retrieve-all repository)]
    (let [all-values (map :value all-ratings)]
      (/ (reduce + all-values) (count all-ratings)))))

(defrecord RatingsComponent [repository]
  Ratings
  (add! [this rating]
    (add-rating! rating repository))
  (count* [this]
    (count-ratings repository))
  (stats [this]
    (rating-stats repository))
  (average [this]
    (average-rating repository)))

(defn new-ratings []
  (map->RatingsComponent {}))









