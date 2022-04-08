(ns bench-rest-test-be.core-test
  (:require [clojure.test :refer :all]
            [bench-rest-test-be.core :refer :all]))

(def mock-transactions [{:Date "2022-04-1" :Amount "10.99"}
                        {:Date "2022-04-1" :Amount "0.99"}
                        {:Date "2022-04-2" :Amount "1.55"}
                        {:Date "2022-04-2" :Amount "100.45"}
                        {:Date "2022-04-2" :Amount "-3.00"}
                        {:Date "2022-04-3" :Amount "-10.00"}
                        {:Date "2022-04-3" :Amount "-50.00"}
                        {:Date "2022-04-4" :Amount "5"}
                        {:Date "2022-04-4" :Amount "-5"}])

(def mock-daily-totals {"2022-04-1" 11.98M
                        "2022-04-2" 99.00M
                        "2022-04-3" -60.00M
                        "2022-04-4" 0M})

(deftest daily-totals-tests
  (testing "Testing calculations of daily totals"
    (is (= (calculate-daily-totals mock-transactions) mock-daily-totals))))
