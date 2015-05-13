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
            [hexagonal-bwertr.interface.data-access :as data-access]))

(def dev-config {:db {:server {:host "localhost"
                               :port 5432
                               :user "bwertr"
                               :schema "bwertr"}
                      :pool {:excess-expire (* 30 60)
                             :expire (* 3 60 60)}}
                 :http {:port 3000}})

(def dev-system (new-system dev-config))

(def dev-system-in-memory-db
  (-> dev-system
      (assoc :data-access (data-access/new-in-memory-ratings-repository))))

(reloaded.repl/set-init! (fn []
                           dev-system-in-memory-db))
