(ns
  ^{:doc "Record query and file metrics that are output periodically"}
  dbexport.metrics
  (:require [metrics.core :refer [new-registry]]
            [metrics.meters :as meter]
            [metrics.counters :as counter]
            [metrics.reporters.console :as console]))

(defn- start-reporting! [reg reporting-type]
  (case reporting-type
    :stdout (console/start (console/reporter reg {}) 5)
    (RuntimeException. (str "reporter " reporting-type " not supported"))))

(defn open
  "Open a metrics context"
  [conf]
  (let [reg (new-registry)]
    (start-reporting! reg :stdout)
    {:records-meter (meter/meter reg "records-meter")
     :records-counter (counter/counter reg "records-counter")
     :bytes-counter (counter/counter reg "bytes-counter")}))

(defn record-read!
  "Mark a record read form the db"
  [{:keys [records-meter records-counter]}]
  (meter/mark! records-meter)
  (counter/inc! records-counter))

(defn bytes-written!
  "Mark the number of bytes written"
  [{:keys [bytes-counter]} byte-count]
  (counter/inc! bytes-counter byte-count))


(defn number-of-records
  "Return the number of records reads"
  [ctx])

(defn bytes-written
  "Return the number of bytes written"
  [ctx])

