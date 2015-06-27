(ns dbexport.export-test
  (:require [clojure.test :refer :all]
            [sjdbc.core :as sjdbc]
            [dbexport.export :as export]
            [dbexport.util :as util]))


(Class/forName "org.hsqldb.jdbcDriver")

(defn setup-db [jdbc user password]
  (let [conn (sjdbc/open jdbc user password {})]
    (sjdbc/exec conn "CREATE TABLE test (id int, name varchar)")
    (dotimes [i 100]
      (sjdbc/exec conn "INSERT INTO test (id, name) values (?, ?)" 1 "abc"))
    conn))

(deftest test-copy []
                   (let [jdbc "jdbc:hsqldb:mem:test"
                         user "sa"
                         pwd nil
                         file (str "target/export-test-out-" (System/currentTimeMillis) ".gz")]
                     (with-open [conn (setup-db jdbc user pwd)]
                       (let [{:keys [latch]}
                             (export/run-export! {:delimiter "," :quote "'" :jdbc jdbc :user user :password pwd
                                                  :file file :query "select * from test"
                                                  :compression :gzip})]
                         @latch))

                     (is (= (util/read-gzip file) "'1','abc'"))))

