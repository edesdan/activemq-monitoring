FROM airdock/oracle-jdk:1.8
ARG ACTIVEMQ_VERSION

ENV ACTIVEMQ_TCP=61616 ACTIVEMQ_STOMP=61613 ACTIVEMQ_UI=8161 ACTIVEMQ_JMX=1099
ENV ACTIVEMQ_HOME /opt/activemq

# Switch to root user to copy additional files
USER root

# Set dir for activemq kahadb
RUN mkdir $ACTIVEMQ_HOME

# Add activemq user and group
RUN groupadd -r activemq -g 1000 && useradd -u 1000 -r -g activemq -m -d /home/activemq -s /sbin/nologin -c "activemq user" activemq

COPY src/docker/target/activemq-$ACTIVEMQ_VERSION.tar.gz $ACTIVEMQ_HOME
RUN cd $ACTIVEMQ_HOME && \
    tar xvf activemq-$ACTIVEMQ_VERSION.tar.gz

# Replace log4j configuration
COPY src/docker/activemq/conf/log4j.properties $ACTIVEMQ_HOME/conf

RUN chown -R activemq:activemq /opt  && \
    chown -Rh activemq:activemq $ACTIVEMQ_HOME && \
    chmod ug+rwx $ACTIVEMQ_HOME/bin/activemq  && \
    chmod ug+rwx $ACTIVEMQ_HOME/bin/activemq-diag


USER activemq

# Sets the working directory for any RUN, CMD, ENTRYPOINT, COPY and ADD instructions that follow it
WORKDIR $ACTIVEMQ_HOME

EXPOSE $ACTIVEMQ_TCP $ACTIVEMQ_STOMP $ACTIVEMQ_UI $ACTIVEMQ_JMX
CMD ["bin/activemq", "console"]