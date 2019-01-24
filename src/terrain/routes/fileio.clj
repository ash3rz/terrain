(ns terrain.routes.fileio
  (:use [common-swagger-api.schema])
  (:require [terrain.util.config :as config]
            [terrain.services.fileio.controllers :as fio]
            [terrain.util :as util]))


(defn secured-fileio-routes
  "The routes for file IO endpoints."
  []
  (util/optional-routes [config/data-routes-enabled]

    (GET "/fileio/download" [:as req]
      (util/controller req fio/download :params))

    (POST "/fileio/upload" [dest :as req]
      (util/controller req fio/upload :params req))

    (POST "/fileio/urlupload" [:as req]
      (util/controller req fio/urlupload :params :body))

    (POST "/fileio/save" [:as req]
      (util/controller req fio/save :params :body))

    (POST "/fileio/saveas" [:as req]
      (util/controller req fio/saveas :params :body))))
