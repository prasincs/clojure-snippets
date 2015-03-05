(ns snippets.data
  [clojure.data.csv :as csv])


(defn seq->csv
  "Converts a lazy seq to a csv "
  [lst outfile-path & {:keys [cols] :or {cols :all}}]
  (let [columns (if (= cols :all)
                  (keys (first lst))
                  cols)
        headers (map name columns)
        rows (mapv #(mapv % columns) lst)]
    (with-open [out-file (clojure.java.io/writer outfile-path)]
      (csv/write-csv out-file (cons headers rows) :quote? (constantly true))))
  )