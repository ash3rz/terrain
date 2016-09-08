FROM clojure:alpine

RUN apk add --update git && \
    rm -rf /var/cache/apk

RUN mkdir -p /etc/iplant/de/crypto && \
    touch /etc/iplant/de/crypto/pubring.gpg && \
    touch /etc/iplant/de/crypto/random_seed && \
    touch /etc/iplant/de/crypto/secring.gpg && \
    touch /etc/iplant/de/crypto/trustdb.gpg
VOLUME ["/etc/iplant/de"]

ARG git_commit=unknown
ARG version=unknown
LABEL org.iplantc.de.terrain.git-ref="$git_commit" \
      org.iplantc.de.terrain.version="$version"

COPY . /usr/src/app
COPY conf/main/logback.xml /usr/src/app/logback.xml

WORKDIR /usr/src/app

RUN lein uberjar && \
    cp target/terrain-standalone.jar .

RUN ln -s "/usr/bin/java" "/bin/terrain"

ENTRYPOINT ["terrain", "-Dlogback.configurationFile=/etc/iplant/de/logging/terrain-logging.xml", "-cp", ".:terrain-standalone.jar", "terrain.core"]
CMD ["--help"]
