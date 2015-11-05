(ns terrain.routes.oauth
  (:use [compojure.core])
  (:require [terrain.clients.apps.raw :as apps]
            [terrain.util.service :as service]))

(defn secured-oauth-routes
  "These routes are callback routes for OAuth authorization codes. They need to be secured because
   we need to associate the access token that we obtain using the authorization code with the
   user. Because of this. These can't be direct callback routes. Instead, the callbacks need to go
   through a servlet in the Discovery Environment backend."
  []
  (routes
   (GET "/oauth/access-code/:api-name" [api-name :as {params :params}]
        (service/success-response (apps/get-oauth-access-token api-name params)))))
