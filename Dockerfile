FROM discoenv/javabase

USER root

RUN mkdir -p /etc/iplant/de/crypto && \
    touch /etc/iplant/de/crypto/pubring.gpg && \
    touch /etc/iplant/de/crypto/random_seed && \
    touch /etc/iplant/de/crypto/secring.gpg && \
    touch /etc/iplant/de/crypto/trustdb.gpg && \
    chown -R iplant:iplant /etc/iplant/de/crypto
VOLUME ["/etc/iplant/de"]

COPY conf/main/logback.xml /home/iplant/
COPY target/terrain-standalone.jar /home/iplant/
RUN chown -R iplant:iplant /home/iplant/

USER iplant
ENTRYPOINT ["java", "-Dlogback.configurationFile=/etc/iplant/de/logging/terrain-logging.xml", "-cp", ".:terrain-standalone.jar", "terrain.core"]
CMD ["--help"]
