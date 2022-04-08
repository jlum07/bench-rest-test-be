(ns bench-rest-test-be.core
  (:require [clj-http.client :as http]
            [cheshire.core :as json])
  (:gen-class))

(defn api-url [page]
  (str "https://resttest.bench.co/transactions/" page ".json"))

(defn get-paginated-api-transactions []
  (loop [data []
         page 1]
    (let [resp (future (http/get (api-url page)))
          {:keys [totalCount transactions]} (json/parse-string (:body @resp) true)
          new-data (concat data transactions)]
      (if (< (count new-data) totalCount)
        (recur new-data (inc page))
        new-data))))

(defn calculate-daily-totals [transactions]
  (reduce (fn [acc {:keys [Date Amount]}]
            (merge-with + acc {Date (read-string Amount)}))
          {}
          transactions))

(defn -main
  [& args]
  (let [transactions (get-paginated-api-transactions)
        daily-totals (calculate-daily-totals transactions)]
    (clojure.pprint/pprint daily-totals)
    )
  )
