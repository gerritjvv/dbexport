(ns dbexport.config)


(def DEFAULT-JDBC "jdbc:mysql://localhost:3306/mysql?connectTimeout=0&socketTimeout=0&autoReconnect=true")
(defonce DEFAULT-DB-USER "root")
(defonce DEFAULT-DB-PASSWORD nil)
(defonce DEFAULT-ENCLOSED nil)
(defonce DEFAULT-DELIMITER ",")
(defonce DEFAULT-COMPRESSION-CODECS #{:gzip :snappy :none})
(defonce DEFAULT-COMPRESSION-CODEC :gzip)

(defn parse-compression-arg [s]
  (let [k (keyword s)]
    (if (DEFAULT-COMPRESSION-CODECS k)
      k
      (throw (ex-info "Unsupported compression codec" {:codec k})))))

(defn parse-query-arg
  "remove any \" in as the first and last character"
  [s]
  (cond
    (and (= (first s) \") (= (last s) \"))  (.substring ^String s 1 (dec (count s)))
    (= (first s) \") (.substring ^String s 1 (count s))
    (= (last s) \")  (.substring ^String s 0 (dec (count s)))
    :default s))