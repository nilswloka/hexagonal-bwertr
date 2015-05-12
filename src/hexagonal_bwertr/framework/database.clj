(ns hexagonal-bwertr.framework.database
  (:require [com.stuartsierra.component :as component])
  (:import [com.mchange.v2.c3p0 ComboPooledDataSource DataSources]))

(defn- make-db-spec [{:keys [host port schema user password] :as server-configuration}]
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname (str "//" host ":" port "/" schema)
   :user user
   :password password})

(defn- make-pooled-db-spec [{:keys [classname subprotocol subname user password] :as db-spec} {:keys [excess-expire expire] :as pool-configuration}]
  (let [pooled-datasource (doto (ComboPooledDataSource.)
                            (.setDriverClass classname)
                            (.setJdbcUrl (str "jdbc:" subprotocol ":" subname))
                            (.setUser user)
                            (.setPassword password)
                            (.setMaxIdleTimeExcessConnections excess-expire)
                            (.setMaxIdleTime expire))]
    {:datasource pooled-datasource}))

(defrecord DatabaseComponent [configuration]
  component/Lifecycle
  (start [{db-spec :db-spec :as this}]
    (if db-spec
      this
      (let [new-db-spec (make-db-spec (-> configuration :db :server))
            pooled-db-spec (make-pooled-db-spec new-db-spec (-> configuration :db :pool))]
        (assoc this :db-spec pooled-db-spec))))
  (stop [{{datasource :datasource} :db-spec :as this}]
    (do
      (DataSources/destroy datasource)
      (assoc this :db-spec nil))))

(defn new-database []
  (map->DatabaseComponent {}))










