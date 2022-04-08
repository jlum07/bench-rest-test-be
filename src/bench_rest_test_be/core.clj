(ns bench-rest-test-be.core
  (:require [clj-http.client :as http]
            [cheshire.core :as json])
  (:gen-class))

(def api-url "https://resttest.bench.co/transactions/")

(defn get-paginated-api-transactions []
  (loop [data []
         page 1]
    (let [resp (future (http/get (str api-url page ".json")))
          {:keys [totalCount transactions] :as body-json} (json/parse-string (:body @resp) true)
          new-data (concat data transactions)]
      ;(clojure.pprint/pprint body-json)
      (if (< (count new-data) totalCount)
        (recur new-data (inc page))
        new-data))))

(defn -main
  [& args]
  (let [transactions (get-paginated-api-transactions)]
    (clojure.pprint/pprint transactions)
    )
  )
