(ns dbexport.utils-test
  (:require [clojure.test :refer :all]
            [dbexport.utils :as utils]))


(deftest testescape []
                    (is (= (utils/escape-string "hi ' \"this\"\tmy") "hi  this my")))

(deftest testescape-number []
                           (is (= (utils/escape-string 1) 1)))

(deftest testquote []
                   (is (= (utils/quote-string "'" "hi") "'hi'")))

(deftest testrow->str []
                      (is (= (utils/vect->str "," "'" ["a" "b"]) "'a','b'")))