FROM airdock/oracle-jdk:1.8
MAINTAINER Daniele De Sanctis, danieledesanctis@gmail.com

ENV PROMETHEUS_VERSION=1.6.1
ENV ARCHIVE=prometheus-${PROMETHEUS_VERSION}.linux-amd64
ENV INSTALL_DIR=.
ENV SERVER_HOME=${INSTALL_DIR}/${ARCHIVE}

RUN curl -o $SERVER_HOME.tar.gz -k -L https://github.com/prometheus/prometheus/releases/download/v$PROMETHEUS_VERSION/prometheus-$PROMETHEUS_VERSION.linux-amd64.tar.gz \
            && tar xvfz ${SERVER_HOME}.tar.gz

WORKDIR ./prometheus-$PROMETHEUS_VERSION.linux-amd64

ADD src/docker/config/prometheus.yml /etc/prometheus/

ENTRYPOINT ./prometheus -config.file=/etc/prometheus/prometheus.yml

EXPOSE 9090




