(ns hexagonal-bwertr.framework.web
  (:require [com.stuartsierra.component :as component]
            [hexagonal-bwertr.interface.controller :as controller]
            [compojure.core :refer [routes GET POST]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [ring.adapter.jetty :refer [run-jetty]]
            [hexagonal-bwertr.enterprise.schemas :as schemas])
  (:import (clojure.lang ExceptionInfo)))

(defn wrap-validation-errors [handler]
  (fn [req]
    (try
      (handler req)
      (catch ExceptionInfo exi
        (if (schemas/validation-error? (ex-data exi))
          {:status 400 :body (pr-str (:error (ex-data exi)))}
          (throw exi))))))

(defn- make-bwertr-routes [controller]
  (-> (routes
       (GET "/" [] (controller/index-controller controller))
       (POST "/" [rating] (controller/rate-with-controller controller rating))
       (GET "/stats" [] (controller/statistics-controller controller)))
      (wrap-defaults site-defaults)
      wrap-validation-errors
      wrap-stacktrace))

(defrecord WebServer [controller configuration]
  component/Lifecycle
  (start [{server :server :as this}]
    (if server
      this
      (assoc this :server (run-jetty (make-bwertr-routes controller) {:port (-> configuration :http :port) :join? false}))))
  (stop [{server :server :as this}]
    (if server
      (.stop server))
    (assoc this :server nil)))

(defn new-web-server []
  (map->WebServer {}))
