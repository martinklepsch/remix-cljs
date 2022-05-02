(ns remix.app
  (:require
   [reagent.core :as r]
   ["@remix-run/react" :refer [NavLink useLoaderData]]))

(defn page-index
  {:export true
   :remix/page "index"}
  []
  (r/as-element
   [:div
    [:h1 "Hi people!"]

    [:p "Welcome to your new Remix app."]
    [:p "Now go build something great with ClojureScript."]

    [:> NavLink {:to "/page-2"} "goto page-2"]]))

(defn page-2-loader
  {:export true}
  []
  (js/Promise.resolve #js {:hello "world"}))

(defn page-2
  {:export true
   :remix/page "page-2"
   :remix/loader page-2-loader}
  []
  (r/as-element
   (let [data (useLoaderData)]
     [:div
      [:h1 "Hello from Page 2"]
      [:p "loader data:"
       [:pre (js/JSON.stringify data nil 2)]]
      [:> NavLink {:to "/"} "back to index"]])))
