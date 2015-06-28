(defproject dbexport "0.1.0"
            :description "db export library to a delimited compressed output"
            :url "https://github.com/gerritjvv/sjdbc"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}

            :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
            :global-vars {*warn-on-reflection* true
                          *assert*             true}

            :jvm-opts ["-Xmx1g"]

            :main dbexport.dbexport
            :aot [dbexport.dbexport]

            :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                           [org.apache.commons/commons-lang3 "3.4"]
                           [sjdbc "0.1.2"]
                           [fun-utils "0.5.9"]
                           [org.xerial.snappy/snappy-java "1.1.2-M1"]
                           [org.clojure/tools.cli "0.3.1"]
                           [org.clojure/tools.logging "0.3.1"]
                           [metrics-clojure "2.4.0"]
                           [mysql/mysql-connector-java "5.1.35"]
                           [hsqldb/hsqldb "1.8.0.10" :scope "test"]])
