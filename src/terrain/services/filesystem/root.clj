(ns terrain.services.filesystem.root
  (:use [clojure-commons.validators])
  (:require [clojure-commons.json :as json]
            [dire.core :refer [with-pre-hook! with-post-hook!]]
            [terrain.clients.data-info.raw :as data-raw]
            [terrain.services.filesystem.common-paths :as paths]))

(defn- format-roots
  [roots user]
  (letfn [(format-label [root] (assoc root :label      (paths/path->label user (:path root))
                                           :hasSubDirs true))
          (update-labels [root-list] (map format-label root-list))]
    (update-in roots [:roots] update-labels)))

(defn do-root-listing
  [{user :user}]
  (-> (data-raw/list-roots user)
      :body
      (json/string->json true)
      (format-roots user)))

(with-pre-hook! #'do-root-listing
  (fn [params]
    (paths/log-call "do-root-listing" params)
    (validate-map params {:user string?})))

(with-post-hook! #'do-root-listing (paths/log-func "do-root-listing"))
