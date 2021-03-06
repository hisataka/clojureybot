(defproject api_sample "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [twitter-api/twitter-api "0.7.7"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [http-kit "2.1.16"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler api-sample.core.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
