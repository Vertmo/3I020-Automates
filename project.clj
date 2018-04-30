(defproject automates "0.1.0-SNAPSHOT"
  :description "Correction de l'examen 2016 de l'UE 3I020 à l'UPMC"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot automates.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
