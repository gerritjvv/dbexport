(ns
  ^{:doc "read data from a jdbc source and write it out to a sink"}
  dbexport.sql
  (:require [sjdbc.core :as sjdbc])
  (:import (java.sql ResultSet)
           (java.util ArrayList)
           (clojure.lang PersistentVector)))

(set! *unchecked-math* true)

(defn rs->seq [^ResultSet rs ^long colcount ^long i]
  (lazy-seq
    (if (< i colcount)
      (cons (.getObject rs (int i))
            (lazy-seq
              (rs->seq rs colcount (inc i)))))))

(defn copy-select!
  "Open a db connection, run a select and write the data to the output f-out as a map withe {col val col2 val2}
   f-out must have format (fn [state] (fn f2 ([s]) ([]) )) After the final row is read f2 is called as (f2)
   "
  [{:keys [jdbc query user password quote delimiter] :as state} f-out]
  {:pre [(string? jdbc) (string? query) (string? user) (or (string? password) (nil? password))
         (string? quote) (string? delimiter)
         (fn? f-out)]}

  (let [conn (sjdbc/open jdbc user password {})
        f2 (f-out state)]
    (try
      (sjdbc/query-with-rs conn query (fn [^ResultSet rs]
                                        (.setFetchSize rs (int 100000))
                                        (let [colcount (inc (.getColumnCount (.getMetaData rs)))]
                                          (while (.next rs)
                                            (f2 (rs->seq rs (long colcount) 1))))
                                        (.close rs)))
      (finally
        (do
          (sjdbc/close conn)
          (f2))))))