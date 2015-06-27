(ns
  ^{:doc "Combines the logic from other namespaces to select from a database
          and write the output into a file"}
  dbexport.export
  (:require [dbexport.sql :as sql]
            [dbexport.fileio :as fileio]
            [dbexport.utils :as utils]
            [dbexport.metrics :as metrics])
  (:import (java.io DataOutputStream)))

(defonce ^"[B" NEW-LINE-BTS (.getBytes "\n" "UTF-8"))

(defn run-export!
  "Run the export of data from a db select into an output file
  Returns {:latch \"a delay latch which can be waited on\" :metrics-ctx \"metrics context\"}"
  [conf]
  (let [latch (promise)
        metrics-ctx (metrics/open conf)]
    (fileio/with-outputstream conf
                              (fn [state]
                                (fn [^DataOutputStream out]
                                  (sql/copy-select! state
                                                    (fn [{:keys [delimiter quote]}]
                                                      (fn
                                                        ([] (deliver latch nil))
                                                        ([row]
                                                         (metrics/record-read! metrics-ctx)
                                                         (let [bts (.getBytes (utils/vect->str delimiter quote row) "UTF-8")]
                                                           (.write out bts)
                                                           (.write out NEW-LINE-BTS)
                                                           (metrics/bytes-written! metrics-ctx (inc (count bts)))))))))))

    {:latch latch :metrics-ctx metrics-ctx}))