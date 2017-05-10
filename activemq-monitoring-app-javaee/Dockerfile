FROM airdock/oracle-jdk:1.8
MAINTAINER Daniele De Sanctis <danieledesanctis@gmail.com>
ARG ACTIVEMQ_VERSION

RUN apt-get update
# Install unzip
RUN apt-get install -y unzip

ENV VERSION 10.1.0.Final
ENV INSTALL_DIR /opt
ENV WILDFLY_HOME ${INSTALL_DIR}/wildfly-${VERSION}
ENV DEPLOYMENT_DIR ${WILDFLY_HOME}/standalone/deployments/

# Create a user and group used to launch processes
# The user ID 1000 is the default for the first "regular" user on Fedora/RHEL,
# so there is a high chance that this ID will be equal to the current user
# making it easier to use volumes (no permission issues)
RUN groupadd -r jboss -g 1000 && useradd -u 1000 -r -g jboss -m -d $WILDFLY_HOME -s /sbin/nologin -c "JBoss user" jboss && \
    chmod 755 $WILDFLY_HOME

# download wildfly distribution
RUN curl -k -O https://download.jboss.org/wildfly/${VERSION}/wildfly-${VERSION}.zip \
    && unzip wildfly-${VERSION}.zip -d ${INSTALL_DIR} \
    && rm wildfly-${VERSION}.zip \
    && rm ${WILDFLY_HOME}/bin/standalone.conf \
    && chmod a+x ${WILDFLY_HOME}/bin/standalone.sh

ADD src/docker/wildfly/standalone ${WILDFLY_HOME}/standalone

# download and deploy the activemq resource adapter
RUN curl -k -o ${DEPLOYMENT_DIR}/activemq-rar.rar http://repo1.maven.org/maven2/org/apache/activemq/activemq-rar/$ACTIVEMQ_VERSION/activemq-rar-$ACTIVEMQ_VERSION.rar \
    && chmod a+w $DEPLOYMENT_DIR/activemq-rar.rar

# copy and deploy app
COPY build/activemq-monitoring-app.war "$DEPLOYMENT_DIR/activemq-monitoring-app.war"

RUN chown -R jboss:jboss ${WILDFLY_HOME}

RUN cd $INSTALL_DIR \
    && ln -s $WILDFLY_HOME/ wildfly

# Set the working directory to jboss' user home directory
WORKDIR $WILDFLY_HOME

# Specify the user which should be used to execute all commands below
USER jboss

CMD $WILDFLY_HOME/bin/standalone.sh -c standalone-full.xml -b 0.0.0.0 -Djava.net.preferIPv4Stack=true

EXPOSE 8080 8443 9990 9993 8787