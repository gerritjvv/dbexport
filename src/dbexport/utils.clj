(ns dbexport.utils
  (:import (org.apache.commons.lang3 StringUtils)))



(defn join
  "Join the objects in v with the separator sep applying f to each item in v"
  [v sep f]
  (StringUtils/join (.iterator ^Iterable (mapv f v)) ^String sep))

(defn quote-string [quote s]
  (.toString
    (doto (StringBuilder.)
      (.append (str quote))
      (.append (str s))
      (.append (str quote)))))

(defn escape-string
  "Escape remove new lines tabs ' and \""
  [s]
  (-> s
      (StringUtils/replace "\n" "")
      (StringUtils/replace "'" "")
      (StringUtils/replace "\t" " ")
      (StringUtils/replace "\"" "")))


(defn vect->str
  "Translates the coll v into a string where each item in row is escaped and delimited by delimiter and quoted by quote"
  [delimiter quote v]
  (join v delimiter (comp (partial quote-string quote) escape-string)))
