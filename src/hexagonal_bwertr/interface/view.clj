(ns hexagonal-bwertr.interface.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.element :refer [link-to]]
            [hiccup.form :refer [form-to submit-button label select-options hidden-field]])
  (:use [ring.middleware.anti-forgery]))

(defn- page [title & content]
  (html5
   [:head
    [:title "bwertr - " title]]
   [:body
    [:nav
     [:ul
      [:li (link-to "/" "home")]
      [:li (link-to "/stats" "statistics")]]]
    [:div#main content]]))

(defn- index-view* []
  (page
   "welcome"
   [:h1 "Welcome to bwertr"]
   [:p "Please rate the presentation below:"]
   [:p
    (form-to [:post "/"]
             (hidden-field "__anti-forgery-token" *anti-forgery-token*)
             (label :rating "Please choose: ")
             [:select#rating {:name :rating}
              (select-options (range 1 11))]
             (submit-button "Rate now!"))]))

(defn- render-row [row]
  [:tr
   [:td (first row)]
   [:td (second row)]])

(defn- statistics-view* [{:keys [number-of-ratings average-rating frequencies]}]
  (page
   "statistics"
   [:h1 "Statistics for this presentation"]
   [:p "There has been a total of " number-of-ratings " votes."]
   [:p "On average, the presentation has been rated with " average-rating "."]
   [:p "The distribution is: "
    [:table
     [:thead
      [:tr
       [:th "Rating"]
       [:th "# of votes"]]]
     [:tbody
      (map render-row frequencies)]]]))

(defn- result-view* [{:keys [number-of-ratings average-rating own-rating]}]
  (page
   "result"
   [:h1 "Thank you ..."]
   [:p "... for rating this presentation with " own-rating "."]
   [:P "In total, this presentation has been rated " number-of-ratings " times with an average rating of " average-rating "."]))

(defprotocol BwertrView
  (index-view [this])
  (statistics-view [this statistics])
  (result-view [this result]))

(defrecord BwertrViewComponent []
  BwertrView
  (index-view [this]
    (index-view*))
  (statistics-view [this statistics]
    (statistics-view* statistics))
  (result-view [this result]
    (result-view* result)))

(defn new-bwertr-view []
  (map->BwertrViewComponent {}))




