package com.edesdan.activemq.monitoring.advisory.control;

import com.edesdan.activemq.monitoring.exceptions.entity.ActiveMQMonitoringRuntimeException;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.DataStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class SlowConsumerAdvisoryListener extends AdvisoryListener {

    private static Logger LOGGER = LoggerFactory.getLogger(SlowConsumerAdvisoryListener.class);

    private transient Session session;

    private MessageConsumer consumer;

    public SlowConsumerAdvisoryListener(Connection connection) {
        super(connection);
    }

    @Override
    public void start() {
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ActiveMQDestination destination = (ActiveMQDestination) session.createQueue(">");
            Destination consumerTopic = AdvisorySupport.getSlowConsumerAdvisoryTopic(destination);
            LOGGER.info("Subscribing to advisory topic: " + consumerTopic);
            consumer = session.createConsumer(consumerTopic);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            throw new ActiveMQMonitoringRuntimeException("error starting consumer:" + this.getClass().getSimpleName(), e);
        }
    }

    @Override
    public void onMessage(Message message) {
        ActiveMQMessage msg = (ActiveMQMessage) message;

        DataStructure data = msg.getDataStructure();

        if (data != null && data.getDataStructureType() == ConsumerInfo.DATA_STRUCTURE_TYPE) {
            ConsumerInfo consumerInfo = (ConsumerInfo) data;
            logConsumerInfo(consumerInfo);
        } else {
            LOGGER.warn("Msg:" + msg.toString());
        }

    }

    private void logConsumerInfo(ConsumerInfo consumerInfo) {

        ActiveMQDestination destination = consumerInfo.getDestination();

        final String subscriptionName = consumerInfo.getSubscriptionName();
        final ConsumerId consumerId = consumerInfo.getConsumerId();
        final String clientId = consumerInfo.getClientId();
        String queueName = "";

        if (destination != null) {
            queueName = destination.getPhysicalName();
        }

        LOGGER.warn("Slow consumer with clientId:'{}', subscriptionName:'{}', consumerId:'{}', detected on queue:'{}'",
                clientId,
                subscriptionName,
                consumerId,
                queueName);

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
