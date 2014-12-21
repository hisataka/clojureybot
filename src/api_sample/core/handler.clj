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
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:require [org.httpkit.client :as http])
  (:require [clojure.xml :as xml])
  (:require [clojure.zip :as zip]))

;clojureybot
;p@ssw0rd
(def my-creds (make-oauth-creds "2sEcph9BOK5Hk412wQ8qJaSI1"
                                "IKHsWgt959b693MTCYc5BYMQOXsDqR4I3m9VXqW0zk7sKvyXdA"
                                "2931060518-ICKKL7AoYhMoCjuztp5f4UVpWmGNMiAItzAwWm1"
                                "CXJECGtp9SW7wA3smz4Gm5ewdm0PQ1VA2gCouBcHC3XDc"))

(defn getTimeline [uname]
  (apply str
   (for [x
         (:body
          (statuses-user-timeline
           :oauth-creds my-creds
           :params {:screen_name uname}))]
     (:text x))
  ))

(defn xml-parse [s]
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))

(defn keyphrase [text] (let [res (http/get (str "http://jlp.yahooapis.jp/KeyphraseService/V1/extract?appid=dj0zaiZpPUlUVGxNS28xUE1ZdSZzPWNvbnN1bWVyc2VjcmV0Jng9MDQ-&sentence=" (java.net.URLEncoder/encode text "UTF-8"))) ]
  (:body @res)))

(defn filKey [text] (apply str (take 3 (for [x (xml-seq(xml-parse (keyphrase text))) :when (= :Keyphrase (:tag x))]
                   (:content x)))))

(defroutes app-routes
  (GET "/" [] "running!")
  (GET "/toldm" {params :params} (str (:code (:status (statuses-update :oauth-creds my-creds
                   :params {:status (str "d " (params :uname) " " (params :msg))})))))
  (GET "/tdm" {params :params} (str (:code (:status (direct-messages-new :oauth-creds my-creds
                     :params {:screen_name (params :uname) :text (params :msg)})))))
  (GET "/getKey" {params :params}
       (filKey
        (getTimeline (params :uname))))
  (GET "/tdm2" {params :params} (str (:code (:status (direct-messages-new :oauth-creds my-creds
                     :params {:screen_name (params :uname) :text
                              (str (params :msg) "\r\n"
                                   (filKey (getTimeline (params :infouname))))})))))

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
