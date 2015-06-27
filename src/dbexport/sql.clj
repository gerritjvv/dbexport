(ns
  ^{:doc "read data from a jdbc source and write it out to a sink"}
  dbexport.sql
  (:require [sjdbc.core :as sjdbc]))


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
      (doseq [row (sjdbc/query conn query)]
        (f2 row))
      (finally
        (do
          (sjdbc/close conn)
          (f2))))))