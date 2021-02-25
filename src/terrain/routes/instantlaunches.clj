(ns terrain.routes.instantlaunches
  (:use [common-swagger-api.schema]
        [ring.util.http-response :only [ok]]
        [terrain.routes.schemas.instantlaunches]
        [common-swagger-api.schema.metadata :only [AvuList AvuListRequest SetAvuRequest]]
        [terrain.auth.user-attributes :only [current-user]]
        [terrain.clients.app-exposer]
        [terrain.util :only [optional-routes]])
  (:require [terrain.util.config :as config]
            [clojure.tools.logging :as log]))

(defn instant-launch-routes
  []
  (optional-routes
   [config/app-routes-enabled]

   (context "/instantlaunches" []
     :tags ["instant-launches"]

     (context "/mappings" []
       (context "/defaults" []

         (GET "/latest" []
           :summary LatestILMappingsDefaultsSummary
           :description LatestILMappingsDefaultsDescription
           :return DefaultInstantLaunchMapping
           (ok (latest-instant-launch-mappings-defaults)))))

     (GET "/" []
       :summary ListInstantLaunchesSummary
       :description ListInstantLaunchesDescription
       :return InstantLaunchList
       (ok (get-instant-launch-list)))

     (context "/:id" []
       :path-params [id :- InstantLaunchIDParam]

       (GET "/" []
         :summary GetInstantLaunchSummary
         :description GetInstantLaunchDescription
         :return InstantLaunch
         (ok (get-instant-launch id)))))))

(defn admin-instant-launch-routes
  []
  (optional-routes
   [config/app-routes-enabled]

   (context "/instant-launches" []
     :tags ["admin-instant-launches"]

     (context "/mappings" []
       (context "/defaults" []
         (PUT "/latest" []
           :summary AddLatestILMappingsDefaultsSummary
           :description AddLatestILMappingsDefaultsDescription
           :body [body InstantLaunchMapping]
           :return InstantLaunchMapping
           (ok (add-latest-instant-launch-mappings-defaults (:username current-user) body)))

         (POST "/latest" []
           :summary UpdateLatestILMappingsDefaultsSummary
           :description UpdateLatestILMappingsDefaultsDescription
           :body [body InstantLaunchMapping]
           :return InstantLaunchMapping
           (ok (update-latest-instant-launch-mappings-defaults (:username current-user) body)))

         (DELETE "/latest" []
           :summary DeleteLatestILMappingsDefaultsSummary
           :description DeleteLatestILMappingsDefaultsDescription
           (ok (delete-latest-instant-launch-mappings-defaults)))))

     (GET "/metadata" []
       :summary ListMetadataSummary
       :description ListMetadataDescription
       :query [query MetadataListingQueryMap]
       :return AvuList
       (ok (list-metadata query)))

     (context "/:id" []
       :path-params [id :- InstantLaunchIDParam]

       (POST "/" []
         :summary UpdateInstantLaunchSummary
         :description UpdateInstantLaunchDescription
         :body [body InstantLaunch]
         :return InstantLaunch
         (ok (update-instant-launch id body)))

       (DELETE "/" []
         :summary DeleteInstantLaunchSummary
         :description DeleteInstantLaunchDescription
         (ok (delete-instant-launch id)))

       (context "/metadata" []
         (GET "/" []
           :summary GetMetadataSummary
           :description GetMetadataDescription
           :return AvuList
           (ok (get-metadata id)))

         (POST "/" []
           :summary UpsertMetadataSummary
           :description UpsertMetadataDescription
           :body [data AvuListRequest]
           :return AvuList
           (ok (upsert-metadata id data)))

         (PUT "/" []
           :summary ResetMetadataSummary
           :description ResetMetadataDescription
           :body [data SetAvuRequest]
           :return AvuList
           (ok (reset-metadata id data)))))

     (PUT "/" []
       :summary AddInstantLaunchSummary
       :description AddInstantLaunchDescription
       :body [body InstantLaunch]
       :return InstantLaunch
       (ok (add-instant-launch (:username current-user) body))))))