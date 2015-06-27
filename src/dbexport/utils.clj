(ns dbexport.utils
  (:import (org.apache.commons.lang3 StringUtils)))


(defn escape-string
  "Escape remove new lines tabs ' and \""
  [s]
  (-> s
      (StringUtils/replace "\n" "")
      (StringUtils/replace "'" "")
      (StringUtils/replace "\t" " ")
      (StringUtils/replace "\"" "")))