# bench-rest-test-be

Code submission for Bench rest test backend

Main code can be found in src/bench_rest_test_be/core.clj

Tests can be found in test/bench_rest_test_be/core_test.clj

## Install Prerequisites

- Clojure: https://clojure.org/guides/getting_started
- Leiningen: https://leiningen.org/

## Running

    $ lein run

## Tests

    $ lein tests

## Assumptions

Assumed that a maximum of 10 transactions per page and that API will fill pages to maximum until last page. 

## TODO

- Would like to get daily total calculations to happen is a more parallel/async manner along with api calls. 
- Add better exception handling
