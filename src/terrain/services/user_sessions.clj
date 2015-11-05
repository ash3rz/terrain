(ns terrain.services.user-sessions
  (:use [slingshot.slingshot :only [try+ throw+]]
        [clojure-commons.error-codes]
        [terrain.util.config]
        [terrain.util.service]
        [terrain.clients.user-sessions]
        [terrain.auth.user-attributes])
  (:require [clojure.string :as string]
            [clojure.tools.logging :as log]
            [clojure-commons.file-utils :as ft]))

(defn user-session
  ([]
     (let [user (:username current-user)]
       (log/debug "Getting user session for" user)
       (success-response (get-session user))))
  ([session]
     (let [user (:username current-user)]
       (log/debug "Setting user session for" user)
       (success-response (set-session user session)))))

(defn remove-session
  []
  (let [user (:username current-user)]
    (log/debug "Deleting user session for" user)
    (success-response (delete-session user))))
