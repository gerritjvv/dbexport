(ns dbexport.dbexport-test
  (:require [clojure.test :refer :all]
            [dbexport.dbexport :as dbexport]))

(deftest test-arguments []
                        (let [{{:keys [
                                       jdbc
                                       user
                                       password
                                       file
                                       compression
                                       delimiter
                                       enclosed
                                       query]} :options}
                              (dbexport/parse-options ["--jdbc=abc"
                                                       "--user=db"
                                                       "--password=pwd"
                                                       "--file=myfile"
                                                       "--compression=gzip"
                                                       "--delimiter=|"
                                                       "--query=\"select * from table\""
                                                       "--enclosed=\""])]

                          (is (= jdbc "abc"))
                          (is (= user "db"))
                          (is (= password "pwd"))
                          (is (= file "myfile"))
                          (is (= compression :gzip))
                          (is (= delimiter "|"))
                          (is (= enclosed "\""))
                          (is (= query "select * from table"))))
