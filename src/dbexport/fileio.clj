(ns
  ^{:doc "Functions for file io"}
  dbexport.fileio)


(defn- create-compression-stream [compressions]
  (switch ))

(defn with-outputstream
  "Call f with a BufferedOutputStream as the only argument
   First f is called with (f state) and returns (f [x] ) that is called"
  [{:keys [compression] :as state} f]
  (let [f2 (f state)]
    (with-open [out (create-output-stream compression)])))
