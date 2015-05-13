(ns user
  (:require [com.stuartsierra.component :as component]
            [reloaded.repl :refer [system init start stop go reset]]
            [clojure.java.io :as io]
            [clojure.java.javadoc :refer [javadoc]]
            [clojure.pprint :refer [pprint]]
            [clojure.reflect :refer [reflect]]
            [clojure.repl :refer [apropos dir doc find-doc pst source]]
            [clojure.set :as set]
            [clojure.test :as test]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [hexagonal-bwertr.system :refer [new-system]]
            [hexagonal-bwertr.interface.data-access :as data-access]
            [hexagonal-bwertr.enterprise.ratings :as ratings]))

(def dev-config {:db {:server {:host "localhost"
                               :port 5432
                               :user "bwertr"
                               :schema "bwertr"}
                      :pool {:excess-expire (* 30 60)
                             :expire (* 3 60 60)}}
                 :http {:port 3000}})

(def dev-system (new-system dev-config))

(defrecord InMemoryAccessComponent [store]
  ratings/RatingsRepository
  (store! [this rating]
    (swap! store conj rating))
  (retrieve-all [this]
    @store))

(defn new-in-memory-ratings-repository []
  (map->InMemoryAccessComponent {:store (atom [])}))

(def dev-system-in-memory-db
  (-> dev-system
      (assoc :data-access (new-in-memory-ratings-repository))))

(reloaded.repl/set-init! (fn []
                           dev-system))
