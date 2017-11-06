(ns terrain.routes.tags
  (:use [compojure.core :only [DELETE GET PATCH POST]])
  (:require [terrain.services.metadata.tags :as tags]
            [terrain.util :as util]
            [terrain.util.config :as config]))


(defn secured-tag-routes
  []
  (util/optional-routes
   [#(and (config/filesystem-routes-enabled) (config/metadata-routes-enabled))]

   (GET "/filesystem/entry/tags" []
     (tags/list-all-attached-tags))

   (GET "/filesystem/entry/:entry-id/tags" [entry-id]
     (tags/list-attached-tags entry-id))

   (PATCH "/filesystem/entry/:entry-id/tags" [entry-id type :as {body :body}]
     (tags/handle-patch-file-tags entry-id type body))

   (GET "/tags/suggestions" [contains limit]
     (tags/suggest-tags contains limit))

   (POST "/tags/user" [:as {body :body}]
     (tags/create-user-tag body))

   (PATCH "/tags/user/:tag-id" [tag-id :as {body :body}]
     (tags/update-user-tag tag-id body))

   (DELETE "/tags/user/:tag-id" [tag-id]
     (tags/delete-user-tag tag-id))))
