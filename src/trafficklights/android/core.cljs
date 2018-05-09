(ns trafficklights.android.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [trafficklights.events]
            [trafficklights.subs]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<! timeout]]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

;;(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
	(get-status))
      ;;(.alert (.-Alert ReactNative) title))


(def app-state (r/atom { 
             :id "",
             :status "2", 
             :lastUpdate "0", ;;last time the device updated the lights
             :now "0", ;;time(ms) on the device now
             :timeRed "0",  
             :timeYellow "0",
             :timeGreen "0",
             :timeNotSafe "40000"})) ;;Not Safe to cross in the last 5 seconds

(defn fetch-json [uri]
  (prn "fetch")
  (-> (js/fetch uri  (clj->js m))
        (.then (fn [r] 
            (.text r)))
        (.then (fn [text]
                 (reset! app-state (js->clj (.parse js/JSON text) :keywordize-keys true))))))

(defn get-status []
   (prn "get-status")
   (let [response (fetch-json "http://192.168.2.13")]))
  
  ;; fetch js
  ;;(-> (js/fetch "http://192.168.2.13"  (clj->js m))
       ;; (.then (fn [r]
        ;;         (if (.-ok r)
                   ;; good
          ;;         (let [content-type (-> r .-headers (.get "content-type"))]
            ;;         (if (clojure.string/includes? content-type "application/json")
              ;;         (.text r)
                ;;       (throw (ex-info "Content-type is not json" {:content-type content-type}))))
                   ;; bad
                  ;; (.then (.text r)
                    ;;      (fn [text]
                      ;;      (let [payload (transit/read transit-reader text)
                        ;;          err {:status (.-status r)
                          ;;             :status-text (.-statusText r)
                            ;;           :failure :error
                              ;;         :response payload}]
                              ;;(throw err)))))))
    ;;    (.then (fn [r] 
     ;;       (.text r)))
     ;;   (.then (fn [text]
    ;;             (let [response (js->clj (.parse js/JSON text) :keywordize-keys true)]
    ;;              (prn response)
    ;;              (reset! app-state response)
    ;;              )))
        ;;(.then handler #(error-handler (if (map? %) % {:message "Fetch promise failed" :reason %})))
        ;; in case of a handler error, break out of the try block
        ;; to throw an actual exception
        ;;(.catch (fn [err] (js/setTimeout #(throw err))))
     ;;   )


  ;; ---------------fetch react----------------------
  ;;(prn ((.fetch ReactNative) "http://192.168.2.13"))
	
  ;; -------------- cljs-http -----------------------
  ;;(go (let [response (<! (http/get "http://192.168.2.13"
  ;;                               {:with-credentials? false}))
  ;;      body (:body response)]
  ;;      (prn (:body response))
  ;;      (prn response)
  ;;      ))
;;)
(enable-console-print!)

;;timer
(go-loop []
  (do
    (get-status)
    (<! (timeout 1000))
    (recur)))

(defn status-to-color[status]
  (cond 
    (= status "0") "#EAD605" ;;yellow going to red
    (= status "1") "#FF0004" ;;red going to yellow
    (= status "2") "#EAD605" ;;yellow going to green
    (= status "3") "#04CF1A" ;;green going to yellow
    :else "#000000")
)

(defn light-to-display[status light]
  (cond 
    (= status light) (status-to-color status)
    (and (= status "2") (= light "0")) (status-to-color status)
    :else "#000000")
)


(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} (str "STATUS:" (:status @app-state))]
       [text {:style {:font-size 100  :line-height 90 :text-align "center" :color (light-to-display (:status @app-state) "1")}} "●"]
       [text {:style {:font-size 100  :line-height 90 :text-align "center" :color (light-to-display (:status @app-state) "0")}} "●"]
       [text {:style {:font-size 100  :line-height 90 :text-align "center" :color (light-to-display (:status @app-state) "3")}} "●"]
       ;;[image {:source {:uri "https://www.seoclerk.com/pics/551103-1TOqFD1502285018.jpg"}
        ;;       :style  {:width 80 :height 80 :margin-bottom 30}}]
       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(alert "HELLO!")}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "update"]]
       ;;[text {:style {:font-size 20 :font-weight "100" :margin-bottom 40 :text-align "center"}} (:status @app-state)]
       ])))

(defn init []
      (dispatch-sync [:initialize-db])
      (.registerComponent app-registry "Trafficklights" #(r/reactify-component app-root)))


