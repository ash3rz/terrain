(ns terrain.routes.schemas.requests
  (:use [common-swagger-api.schema :only [describe NonBlankString]]
        [schema.core :only [Any defschema enum optional-key]])
  (:require [schema-tools.core :as st])
  (:import [java.util UUID]))

(def RequestId (describe UUID "The request ID"))

(defschema RequestListingQueryParams
  {(optional-key :include-completed)
   (describe Boolean "If set to true, completed requests will be included in the listing")

   (optional-key :request-type)
   (describe String "If specified, only requests of the selected type will be included in the listing")

   (optional-key :requesting-user)
   (describe String "If specified, only requests submitted by the selected user will be included in the listing")})

(defschema RequestUpdate
  {:created_date  (describe NonBlankString "The date and time the update occurred")
   :id            (describe UUID "The update ID")
   :message       (describe String "The message entered by the person who updated the requst")
   :status        (describe String "The request status code")
   :updating_user (describe String "The username of the person who updated the request")})

(defschema Request
  {:id              RequestId
   :request_type    (describe NonBlankString "The name of the request type")
   :requesting_user (describe NonBlankString "The username of the requesting user")
   :details         (describe Any "The request details")
   :updates         (describe [RequestUpdate] "Updates that were made to the request")})

(defschema RequestListing
  {:requests (describe [(st/dissoc Request :updates)] "A listing of administrative requests")})

(defschema RequestUpdateMessage
  {(optional-key :message)
   (describe NonBlankString "The message to store with the request.")})

(defschema ViceRequestDetails
  {(optional-key :name)
   (describe NonBlankString "The user's name")

   :institution
   (describe NonBlankString "The name of the institution that user works for")

   (optional-key :email)
   (describe NonBlankString "The user's email address")

   :intended_use
   (describe NonBlankString "The reason for requesting VICE access")

   :funding_award_number
   (describe NonBlankString "The award number from any relevant funding agency")

   :references
   (describe [NonBlankString] "The names of other CyVerse users who can vouch for the user")

   :orcid
   (describe NonBlankString "The user's ORCID identifier")

   :concurrent_jobs
   (describe Integer "The requested number of concurrently running VICE jobs")})

(defschema ViceRequest
  (st/assoc Request
            :details (describe ViceRequestDetails "The request details")))

(defschema ViceRequestListing
  {:requests (describe [(st/dissoc ViceRequest :updates)] "A listing of VICE access requests")})