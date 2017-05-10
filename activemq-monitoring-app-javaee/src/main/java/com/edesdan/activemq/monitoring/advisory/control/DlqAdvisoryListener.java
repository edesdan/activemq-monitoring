package com.edesdan.activemq.monitoring.advisory.control;

import com.edesdan.activemq.monitoring.exceptions.entity.ActiveMQMonitoringRuntimeException;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import java.io.IOException;

public class DlqAdvisoryListener extends AdvisoryListener {

    private static Logger LOGGER = LoggerFactory.getLogger(DlqAdvisoryListener.class);

    private transient Session session;

    private MessageConsumer consumer;

    public DlqAdvisoryListener(Connection connection) {
        super(connection);
    }


    public void start() {
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ActiveMQDestination destination = (ActiveMQDestination) session.createQueue(">");
            Destination consumerDlqDestination = AdvisorySupport.getMessageDLQdAdvisoryTopic(destination);
            LOGGER.info("Subscribing to advisory topic: {}, destination type: {}", consumerDlqDestination, destination.getDestinationTypeAsString());
            consumer = session.createConsumer(consumerDlqDestination);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            throw new ActiveMQMonitoringRuntimeException("error starting consumer:" + this.getClass().getSimpleName(), e);
        }
    }

    @Override
    public void onMessage(Message message) {

        ActiveMQMessage msg = (ActiveMQMessage) message;
        logDlqMessageInfo(msg);

    }

    private void logDlqMessageInfo(ActiveMQMessage msg) {
        ActiveMQDestination advisoryDestination = msg.getDestination();
        String advisoryDestinationName = null;
        String brokerName = null;
        String brokerUrl = null;


        if (advisoryDestination != null) {
            advisoryDestinationName = advisoryDestination.getPhysicalName();
        }

        try {
            brokerName = (String) msg.getProperty("originBrokerName");
            brokerUrl = (String) msg.getProperty("originBrokerURL");

        } catch (IOException e) {

        }


        LOGGER.warn("DLQ message received on:'{}' from broker name:'{}' with brokerURL:'{}' ", advisoryDestinationName, brokerName, brokerUrl);

        // log all in debug
        LOGGER.debug(msg.toString());

    }


    @Override
    public void close() throws Exception {
        try {
            consumer.close();
            session.close();
        } catch (JMSException e) {
            throw new ActiveMQMonitoringRuntimeException("error shutting down session for consumer " + this.getClass().getSimpleName(), e);
        }
    }
}