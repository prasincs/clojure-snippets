(ns snippets.channels
  (:require [clojure.core.async :as async]
            [clojure.tools.logging :as log]
            [hdfs.core :as hdfs])
  (:import (java.io IOException)))


(def writer-chan (async/chan))



(defn start-hadoop-file-writer
  "Starts a thread that listens on the writer channel and writes to the files"
  [out-directory]
  (async/thread
    (let [fs (hdfs/filesystem out-directory)
          writers-map (atom {})
          cleanup-fn (fn []
                       (log/info "Initiating Cleanup for Hadoop File Writer")
                       (doseq [[name writer] @writers-map]
                         (log/info "Closing writer for " name)
                         (. writer close)
                         )
                       (. fs close)
                       (log/info "Done Cleaning up Hadoop File Writer")
                       )]
      (try
        (loop [data (async/<!! writer-chan)]
          (if (not= :complete data)
            (do (let [{:keys [content day path]} data]
                  (println "Would write to " path)
                  )
                (recur (async/<!! writer-chan)
                       ))))
        (catch IOException e
          (log/error e "Closing all channels")
          (cleanup-fn)
          )
        (finally
          (cleanup-fn))))))


(defn stop-hadoop-file-writer []
  (async/put! writer-chan :complete))