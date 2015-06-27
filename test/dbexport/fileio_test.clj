(ns dbexport.fileio-test
  (:require [clojure.test :refer :all]
            [dbexport.fileio :as fileio]
            [clojure.java.io :as io])
  (:import (java.io BufferedOutputStream InputStream FileInputStream DataOutputStream BufferedInputStream BufferedReader InputStreamReader)
           (java.util.zip GZIPInputStream)
           (org.xerial.snappy SnappyInputStream)))


(defn read-file [^InputStream in]
  (let [buff (StringBuilder.)]
    (with-open [in (BufferedReader. (InputStreamReader. in))]
      (loop []
        (if-let [line (.readLine in)]
          (.append buff line)
          (recur))))
    (.toString buff)))

(defn read-gzip [file]
  (read-file (GZIPInputStream. (FileInputStream. (io/file file)))))

(defn read-snappy [file]
  (read-file (SnappyInputStream. (FileInputStream. (io/file file)))))

(deftest test-gzip []
                   (let [file (str "target/test-gzip-" (System/currentTimeMillis) ".gz")]
                     (fileio/with-outputstream {:compression :gzip :file file}
                                               (fn [_]
                                                 (fn [^DataOutputStream out]
                                                   (.write out (.getBytes "abc" "UTF-8")))))

                     (is (= (read-gzip file) "abc"))))

(deftest test-snappy []
                      (let [file (str "target/test-snappy-" (System/currentTimeMillis))]
                        (fileio/with-outputstream {:compression :snappy :file file}
                                                  (fn [_]
                                                    (fn [^BufferedOutputStream out]
                                                     (.write out (.getBytes "abc" "UTF-8")))))

                        (is (= (read-snappy file) "abc"))))

