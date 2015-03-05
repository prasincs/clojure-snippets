(ns snippets.output
  (:require [table.core :as tab]))


(defn print-tables [] (tab/table  [{:file "part-0001" :bytes 1177649935 :buffer-size 1024 :time 2520.988}
                                   {:file "part-0001" :bytes 1177649935 :buffer-size 2048 :time 1767.391}
                                   {:file "part-0001" :bytes 1177649935 :buffer-size 4096 :time 1754.415}
                                   {:file "part-0001" :bytes 1177649935 :buffer-size 8192 :time 1688.19}
                                   {:file "part-0001" :bytes 1177649935 :buffer-size 16384 :time 1677.923}
                                   {:file "part-0001" :bytes 1177649935 :buffer-size 32768 :time 1692.904}
                                   ]
                                  :style :github-markdown))
