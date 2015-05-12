(ns hexagonal-bwertr.framework.database
  (:require [com.stuartsierra.component :as component])
  (:import [com.mchange.v2.c3p0 ComboPooledDataSource DataSources]))

(defn- make-db-spec
  "
  Creates a database specification from configuration.
  
  Expects the following keys:
  :host The Postgres server's host name
  :port The Postgres server's port
  :schema The name of the database to be used
  :user The username used for connecting to the database server
  :password The password used for connecting to the database server
  "
  [{:keys [host port schema user password] :as server-configuration}]
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname (str "//" host ":" port "/" schema)
   :user user
   :password password})

(defn- make-pooled-db-spec
  "
  Creates a pooled database specification from an unpooled database specification and a pool configuration.

  Expects the following keys to be present in the pool configuration:
  :excess-expire Idle time in seconds until excess connections are closed
  :expire Idle time in seconds until connections are closed
  "
  [{:keys [classname subprotocol subname user password] :as db-spec} {:keys [excess-expire expire] :as pool-configuration}]
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

(defn new-database
  "Creates a new, unconfigured instance of DatabaseComponent."
  []
  (map->DatabaseComponent {}))
