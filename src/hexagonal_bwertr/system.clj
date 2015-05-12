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
       :configuration configuration
       :database (database/new-database)
       :data-access (data-access/new-data-access)
       :ratings (ratings/new-ratings)
       :bwertr (bwertr/new-bwertr-application)
       :controller (controller/new-bwertr-controller)
       :view (view/new-bwertr-view)
       :web (web/new-web-server))
      (component/system-using
       {:database [:configuration]
        :data-access [:database]
        :ratings {:repository :data-access}
        :bwertr [:ratings]
        :controller [:bwertr :view]
        :web [:controller :configuration]})))




















