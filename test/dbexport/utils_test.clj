(ns dbexport.utils-test
  (:require [clojure.test :refer :all]
            [dbexport.utils :as utils]))


(deftest testescape []
                    (is (= (utils/escape-string "hi ' \"this\"\tmy") "hi  this my")))
