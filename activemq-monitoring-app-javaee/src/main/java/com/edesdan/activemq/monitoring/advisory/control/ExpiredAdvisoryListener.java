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

public class ExpiredAdvisoryListener  extends AdvisoryListener {

    private static Logger LOGGER = LoggerFactory.getLogger(ExpiredAdvisoryListener.class);

    private transient Session session;

    private MessageConsumer consumer;

    public ExpiredAdvisoryListener(Connection connection) {
        super(connection);
    }

    @Override
    public void start() {
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ActiveMQDestination destination = (ActiveMQDestination) session.createTopic(">");
            Destination consumerTopic = AdvisorySupport.getExpiredMessageTopic(destination);
            LOGGER.info("Subscribing to advisory topic: " + consumerTopic);
            consumer = session.createConsumer(consumerTopic);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            throw new ActiveMQMonitoringRuntimeException("error starting consumer:" + this.getClass().getSimpleName(), e);
        }
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

    @Override
    public void onMessage(Message message) {
        ActiveMQMessage msg = (ActiveMQMessage) message;

        LOGGER.warn("Msg:" + msg.toString());

    }

}