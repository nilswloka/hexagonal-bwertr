(ns hexagonal-bwertr.system
  (:require [com.stuartsierra.component :as component]
            [hexagonal-bwertr.framework.database :as database]
            [hexagonal-bwertr.framework.web :as web]
            [hexagonal-bwertr.interface.data-access :as data-access]
            [hexagonal-bwertr.interface.controller :as controller]
            [hexagonal-bwertr.interface.view :as view]
            [hexagonal-bwertr.enterprise.ratings :as ratings]
            [hexagonal-bwertr.application.bwertr :as bwertr]))

(defn new-system [configuration]
  (-> (component/system-map
       ;; Configuration
       :configuration configuration
       ;; Framework layer
       :database (database/new-database)
       :web (web/new-web-server)
       ;; Interface layer
       :data-access (data-access/new-data-access)
       :controller (controller/new-bwertr-controller)
       :view (view/new-bwertr-view)
       ;; Application domain layer
       :bwertr (bwertr/new-bwertr-application)
       ;; Enterprise domain layer
       :ratings (ratings/new-ratings))
      (component/system-using
       {:database [:configuration]
        :data-access [:database]
        :ratings {:repository :data-access}
        :bwertr [:ratings]
        :controller [:bwertr :view]
        :web [:controller :configuration]})))




















