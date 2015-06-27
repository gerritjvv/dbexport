(ns dbexport.dbexport
  (:require [clojure.tools.cli :refer [parse-opts]]
            [dbexport.config :as config]
            [clojure.string :as string]
            [dbexport.export :as export]
            [dbexport.metrics :as metrics])
  (:gen-class))

(def cli-options
  [["-J" "--jdbc jdbc-url" "JDBC url ensure that the correct jdbc drivers are on the classpath
                            mysql: jdbc:mysql://localhost:3306/mysql?connectTimeout=0&socketTimeout=0&autoReconnect=true
                            postgres: jdbc:postgresql://host:port/database"
    :default config/DEFAULT-JDBC
    :default-desc "localhost"]

   ["-u" "--user user" "DB user"
    :default config/DEFAULT-DB-USER]

   ["-p" "--password password" "DB password"
    :default config/DEFAULT-DB-PASSWORD]

   ["-q" "--query select-query" "db select query to use in the export"
    :parse-fn config/parse-query-arg]

   ["-f" "--file filename" "The file to output the data to"]
   ["-c" "--compression compression" (str "Can be " (map str config/DEFAULT-COMPRESSION-CODECS))
    :defualt config/DEFAULT-COMPRESSION-CODEC
    :parse-fn config/parse-compression-arg]

   ["-D" "--delimiter delimiter" "What field separator default is ,"
    :default config/DEFAULT-DELIMITER]
   ["-E" "--enclosed enclosed" "Character to use to enclose fields default is nil"
    :default config/DEFAULT-ENCLOSED]

   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["dbexport"
        ""
        "Usage: dbexport [options]"
        ""
        "Options:"
        options-summary
        ""
        "Please refer to the manual page for more information."]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn run-export! [{:keys [threads iterations] :as conf}]
  (let [start (System/currentTimeMillis)
        {:keys [latch metrics-ctx]} (export/run-export! conf)]
    @latch
    (prn "Read " (metrics/number-of-records metrics-ctx) " in " (- (System/currentTimeMillis) start) "ms")))

(defn parse-options [args]
  (parse-opts args cli-options))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-options args)]
    ;; Handle help and error conditions
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-msg errors))
      :default (run-export! options))

    (System/exit 0)))