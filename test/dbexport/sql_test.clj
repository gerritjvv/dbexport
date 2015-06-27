(ns dbexport.sql-test
  (:require [clojure.test :refer :all]
            [dbexport.sql :as sql]
            [sjdbc.core :as sjdbc]))

(Class/forName "org.hsqldb.jdbcDriver")

(defn setup-db [jdbc user password]
  (let [conn (sjdbc/open jdbc user password {})]
    (sjdbc/exec conn "DROP TABLE IF EXISTS test")
    (sjdbc/exec conn "CREATE TABLE test (id int, name varchar)")
    (dotimes [i 100]
      (sjdbc/exec conn "INSERT INTO test (id, name) values (?, ?)" 1 "abc"))
    conn))

(deftest test-copy []
                   (let [counter (atom 0)
                         jdbc "jdbc:hsqldb:mem:test"
                         user "sa"
                         pwd nil]
                     (with-open [conn (setup-db jdbc user pwd)]
                       (sql/copy-select! {:quote "'" :delimiter "," :jdbc jdbc :query "select * from test"
                                          :user user :pwd pwd}
                                         (fn [_]
                                           (fn
                                             ([])
                                             ([row-str]
                                              (swap! counter inc))))))

                     (is (= @counter 100))))

