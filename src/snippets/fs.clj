(ns snippets.fs
  (:import (java.io RandomAccessFile)
           (java.nio.channels FileChannel$MapMode)))




(defn read-file-mmap
  "Randomly access a file and memory map it."
  [filePath]
  (let [file (RandomAccessFile. filePath "r")
        file-channel (.getChannel file)
        mapped-byte-buf (.map file-channel
                              FileChannel$MapMode/READ_ONLY
                              0
                              (.length file))
        ; reading explicitly because just calling capacity isn't enough
        bytes-count (.capacity mapped-byte-buf)
        ]
    (loop [i 0]
      (let [c (.get mapped-byte-buf)]
        (if (and (not= c -1)
                 (.hasRemaining mapped-byte-buf))
          (recur
            (inc i))
          (do
            (.clear mapped-byte-buf)
            (.close file-channel)
            (.close file)
            (println filePath " " i " bytes")
            )

          )))
    ))


(defn count-bytes-per-line [filepath & {:keys [encoding] :or {encoding "UTF-8"}}]
  (with-open [rdr (io/reader filepath)]
    (doall (map #(alength (.getBytes % encoding))
                (line-seq rdr)))))


(defn read-file-buffered-reader-one-byte [filepath]
  (let [fr (FileReader. filepath)
        br (BufferedReader. fr)]
    (loop [val (.read br)
           c 0]
      #_(if (zero? (mod c 10000000))
        (println c))
      (if (not= val -1)
        (recur
          (.read br)
          (inc c))
        (do
          (println filepath " " c " bytes")
          (.close br)
          (.close fr)))))
  )


(defn write-bytes-to-gzip
  [filepath bytes & {:keys [append] :or {append false}}]
  (with-open [w (GZIPOutputStream. (io/output-stream
                                     filepath
                                     :append append
                                     ))]
    (.write w bytes)))