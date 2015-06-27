(ns dbexport.fileio-test
  (:require [clojure.test :refer :all]
            [dbexport.fileio :as fileio]
            [dbexport.util :as util])
  (:import (java.io BufferedOutputStream DataOutputStream)))



(deftest test-gzip []
                   (let [file (str "target/test-gzip-" (System/currentTimeMillis) ".gz")]
                     (fileio/with-outputstream {:compression :gzip :file file}
                                               (fn [_]
                                                 (fn [^DataOutputStream out]
                                                   (.write out (.getBytes "abc" "UTF-8")))))

                     (is (= (util/read-gzip file) "abc"))))

(deftest test-snappy []
                      (let [file (str "target/test-snappy-" (System/currentTimeMillis))]
                        (fileio/with-outputstream {:compression :snappy :file file}
                                                  (fn [_]
                                                    (fn [^BufferedOutputStream out]
                                                     (.write out (.getBytes "abc" "UTF-8")))))

                        (is (= (util/read-snappy file) "abc"))))

