(ns ^:figwheel-hooks hello_world.app
  (:require [ajax.core :as ajax]
            [ajax.core :refer [GET POST]]
            [axios]
            [bootstrap :as bootstrap]
            [reagent.core :as reagent]
            [reagent.dom :as dom]
            ))

(defn menu-item [{:keys [label message] :as item}]
      [:li.menu-item {:on-click (fn [] (if (= label "seduro.com")
                                         (set! js/window.location.href "https://seduro.com")
                                         (do
                                           (set! (.-textContent (js/document.getElementById "new")) label)
                                           (js/console.log item))
                                         ))}
       label])


(defn menu2 []
      [:div.row
       [:h1 "Menu Example"]]
      [:ul.menu
       [menu-item {:label "Home" :message "You clicked on Home"}]
       [menu-item {:label "About" :message "You clicked on About"}]
       [menu-item {:label "Contact" :message "You clicked on Contact"}]
       [menu-item {:label "seduro.com" :message "isSED: You clicked on seduro.com"}]
       ]
      )


(defn render-html []
      [:div
       [:svg.bi {:width "24" :height "24"}
        [:use {:href "bootstrap-icons.svg#filetype-js"}]
        ]

       [:svg.bi {:width "24" :height "24"}
        [:use {:href "bootstrap-icons.svg#filetype-rb"}]
        ]

       [:svg.bi {:width "24" :height "24"}
        [:use {:href "bootstrap-icons.svg#database"}]
        ]
       ])


(defn menu-item-clicked []
      [:h3 {:style {:display "flex" :color "gray"}}
       [:p {:style {:min-width "10em"}} "You clicked:"]
       [:p#new]])

(defn footer []
      [:footer
       {:class "footer mt-auto py-3 bg-light"}
       [:div.container.justify-content-center.d-flex {:style {:gap "5em"}}
        [:span "© 2023"
         ]
        [:a {:href "https://seduro.com"} "www.seduro.com"]
        [render-html]
        [:a {:href "https://github.com/SeduroDotCom/Clojure-RubyOnRails-Postgres-Overview"}
         [:svg.bi {:width "24" :height "24"}
          [:use {:href "bootstrap-icons.svg#github" :target "_blank"}]
          ]
         ]
        ]
       [:style {:type "text/css"} "body {margin-bottom: 60px;}"]
       ])


(defn log-response [response]
      (let [json (js/JSON.parse (:responseText response))]
           (js/console.log "Response status:" (:status response))
           (js/console.log "Response body:" json)
           ))


; TABLE HANDLING START
(defn generate-table-row [data]
      (let [id (.-id data)
            name (.-name data)
            brand (.-brand data)
            price (.-price data)
            description (.-description data)
            created-at (.-created_at data)
            updated-at (.-updated_at data)
            ]
           (str "<tr>"
                "<td>" id "</td>"
                "<td>" name "</td>"
                "<td>" brand "</td>"
                "<td>" price "</td>"
                "<td>" description "</td>"
                "<td>" created-at "</td>"
                "<td>" updated-at "</td>"
                "<td><button type=\"button\" class=\"btn btn-danger\" onClick=\"window.deleteProduct(" id " )\" id=\"delete-row-" id " \">Delete</button></td>"
                "</tr>")))

(defn update-table [data]
      (let [body (.getElementById js/document "my-body")]
           (set! (.-innerHTML body)
                 (apply str (map generate-table-row data)))))

(defn make-request-and-log-it []
      (-> (.. axios (get "http://35.222.209.189:3000/api/v1/products"))
          (.then #(update-table (.-data %)))
          (.catch #(js/console.log %))))

(make-request-and-log-it)
; TABLE HANDLING END


; BOOTSTRAP-COMPONENT START
(defn bootstrap-component []
      [:div.container
       [:h1 "Hello, Bootstrap!"]
       [:p "This is a Bootstrap component."]
       [:div.d-flex
        [:p {:style {:min-width "12em"}} "Current menu selected:"]
        [:div#new {:style {:font-weight "800" :color "green"}}]
        ]
       ])
; BOOTSTRAP-COMPONENT END

; BOOTSTRAP-COMPONENT START
(defn info-component []
      [:div.container
       [:h1 "Brief overview"]
       [:ul
        [:li "ClojureScript-Rails-Postgres Prototype"]
        [:li "Hosted on GoogleCloud-Platform"]
        ]
       ])
; BOOTSTRAP-COMPONENT END


; INPUT-FORM START
(defn reset-form-fields []
      (doseq [field (js/document.querySelectorAll "form input")]
             (aset field "value" "")))

(defn send-form-data [data]
      (let [xhr (js/XMLHttpRequest.)]
           (.open xhr "POST" "http://35.222.209.189:3000/api/v1/products")
           (.setRequestHeader xhr "Content-Type" "application/json")
           (.addEventListener xhr "load" #(do
                                            (js/console.log "Response:" %)
                                            (make-request-and-log-it)
                                            (reset-form-fields))
                              )
           (.addEventListener xhr "error" #(js/console.log "Error occurred during the request."))
           (.send xhr (js/JSON.stringify data))))

(defn form-component []
      [:div.container
       [:h1 "Submit Form"]
       [:form {:id        "my-form"
               :on-submit #(do
                             (.preventDefault %)
                             (let [name (.-value (js/document.getElementById "name"))
                                   description (.-value (js/document.getElementById "description"))
                                   price (.-value (js/document.getElementById "price"))
                                   brand (.-value (js/document.getElementById "brand"))

                                   data (clj->js {:name        name
                                                  :description description
                                                  :price       price
                                                  :brand       brand})
                                   ]
                                  (do
                                    (js/console.log "Request:" data)
                                    (send-form-data data)
                                    )
                                  ))}
        [:div.form-group
         [:label {:for "name"} "Name:"]
         [:input.form-control {:type "text" :id "name"}]]
        [:div.form-group
         [:label {:for "description"} "Description:"]
         [:input.form-control {:type "text" :id "description"}]]
        [:div.form-group
         [:label {:for "price"} "Price:"]
         [:input.form-control {:type "text" :id "price"}]]
        [:div.form-group
         [:label {:for "brand"} "Brand:"]
         [:input.form-control {:type "text" :id "brand"}]]
        [:button.btn.btn-primary {:type "submit"} "Submit"]]])
; INPUT-FORM END


(defn app []
      [:div
       [:div.container
        [:div {:style {:min-height "2em"}}]

        [menu2 {:style {:font-size "20px"}}]
        [:div {:style {:min-height "2em"}}]

        [info-component]
        [:div {:style {:min-height "2em"}}]

        [bootstrap-component]
        [:div {:style {:min-height "2em"}}]

        [form-component]
        [:div {:style {:min-height "8em"}}]

        [:table#my-table.table
         [:thead.thead-dark
          [:tr
           [:th {:scope "col"} "#"]
           [:th {:scope "col"} "Name"]
           [:th {:scope "col"} "Brand"]
           [:th {:scope "col"} "Price"]
           [:th {:scope "col"} "Description"]
           [:th {:scope "col"} "Created"]
           [:th {:scope "col"} "Updated"]
           ]
          ]
         [:tbody#my-body]
         ]


        [:div {:style {:min-height "2em"}}]
        [:div.container [footer]]
        ]])


(defn render-app [] (dom/render [app] (js/document.getElementById "hello_world_container")))

(defn ^:after-load re-render [] (render-app))

(defonce init-app (do (render-app) true))


