FROM debian:jessie

ENV VERSION=4.2.0
ENV DOWNLOAD_URL="https://s3-us-west-2.amazonaws.com/grafana-releases/release/grafana_${VERSION}_amd64.deb"

RUN apt-get update && \
    apt-get -y --no-install-recommends install libfontconfig curl ca-certificates && \
    apt-get clean && \
    curl -k ${DOWNLOAD_URL} > /tmp/grafana.deb && \
    dpkg -i /tmp/grafana.deb && \
    rm /tmp/grafana.deb && \
    curl -L https://github.com/tianon/gosu/releases/download/1.7/gosu-amd64 > /usr/sbin/gosu && \
    chmod +x /usr/sbin/gosu && \
    apt-get autoremove -y && \
    rm -rf /var/lib/apt/lists/*

# copy custom data (graphana db)
ADD src/docker/graphana/data /var/lib/grafana

# copy configuration file
ADD src/docker/graphana/config/grafana.ini /etc/grafana

# create dashboards dir
RUN mkdir /var/lib/grafana/dashboards

# copy dashboards
ADD src/docker/graphana/dashboards /var/lib/grafana/dashboards

ADD src/docker/graphana/dashboards /usr/share/grafana/public/dashboards

COPY src/docker/graphana/run.sh /run.sh

RUN chown -R grafana:grafana /run.sh \
    && chmod u+x /run.sh

CMD ["/run.sh"]

EXPOSE 3000




