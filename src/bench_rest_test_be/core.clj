(ns bench-rest-test-be.core
  (:require [clj-http.client :as http]
            [cheshire.core :as json])
  (:gen-class))

(defn api-url [page]
  (str "https://resttest.bench.co/transactions/" page ".json"))

(defn parse-response-body [resp]
  ;TODO - Find a better place/way for status code handling
  (if (= (:status resp) 200)
    (-> (:body resp)
        (json/parse-string true))
    (throw (Exception. "Error - API returned non 200 code"))))

(defn get-paginated-api-transactions-async []
  "Calls first page of api to calculate number of pages to call, then calls the remaining pages asynchronously.
  Assumes 10 transactions per page max."
  (let [response-1 (future (http/get (api-url 1)))
        {:keys [totalCount transactions]} (parse-response-body @response-1)
        pages (Math/ceil (/ totalCount 10))]
    (if (> pages 1)
        (let [remaining-pages (range 2 (inc pages))
              rest-responses (future (map (fn [page] (http/get (api-url page))) remaining-pages))
              rest-transactions (future (mapcat (fn [resp] (:transactions (parse-response-body resp))) @rest-responses))]
          (concat transactions @rest-transactions))
        transactions)))

(defn calculate-daily-totals [transactions]
  "Returns a map with keys as the Date and values as the daily transactions totals for those days."
  (reduce (fn [acc {:keys [Date Amount]}]
            ;BigDecimal fix for floating point precision error
            ;(+ 20000 -10.99 -35.7) ==> 19953.309999999998 ... which is not right
            (merge-with + acc {Date (BigDecimal. Amount)}))
          {}
          transactions))

(defn -main
  [& args]
  (let [transactions (get-paginated-api-transactions-async)
        daily-totals (calculate-daily-totals transactions)]
    (clojure.pprint/pprint daily-totals)
    daily-totals))
