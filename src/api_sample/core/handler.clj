(ns api-sample.core.handler
  (:use
   [twitter.oauth]
   [twitter.callbacks]
   [twitter.callbacks.handlers]
   [twitter.api.restful])
  (:import
   (twitter.callbacks.protocols SyncSingleCallback))
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

;clojureybot
;p@ssw0rd
(def my-creds (make-oauth-creds "2sEcph9BOK5Hk412wQ8qJaSI1"
                                "IKHsWgt959b693MTCYc5BYMQOXsDqR4I3m9VXqW0zk7sKvyXdA"
                                "2931060518-ICKKL7AoYhMoCjuztp5f4UVpWmGNMiAItzAwWm1"
                                "CXJECGtp9SW7wA3smz4Gm5ewdm0PQ1VA2gCouBcHC3XDc"))

(defroutes app-routes
  (GET "/" [] "running!")
  (GET "/toldm" {params :params} (str (:code (:status (statuses-update :oauth-creds my-creds
                   :params {:status (str "d " (params :uname) " " (params :msg))})))))
  (GET "/tdm" {params :params} (str (:code (:status (direct-messages-new :oauth-creds my-creds
                     :params {:screen_name (params :uname) :text (params :msg)})))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
