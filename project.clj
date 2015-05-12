(defproject hexagonal-bwertr "0.1.0-SNAPSHOT"
  :description "A sample project demonstrating hexagonal architecture with Clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 ;; Database
                 [yesql "0.4.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [com.mchange/c3p0 "0.9.5"]
                 [org.postgresql/postgresql "9.3-1103-jdbc4"]
                 ;; Web
                 [hiccup "1.0.4"]
                 [compojure "1.3.4"]
                 [ring/ring "1.3.2"]
                 [ring/ring-defaults "0.1.3"]
                 ;; Architecture
                 [com.stuartsierra/component "0.2.3"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [reloaded.repl "0.1.0"]]
                   :source-paths ["dev"]}})
