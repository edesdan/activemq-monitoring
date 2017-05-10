package com.edesdan.activemq.monitoring.advisory.control;

import com.edesdan.activemq.monitoring.exceptions.entity.ActiveMQMonitoringRuntimeException;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.ProducerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class FastProducerAdvisoryListener extends AdvisoryListener {

    private static Logger LOGGER = LoggerFactory.getLogger(FastProducerAdvisoryListener.class);

    private transient Session session;

    private MessageConsumer consumer;

    public FastProducerAdvisoryListener(Connection connection) {
        super(connection);
    }

    @Override
    public void start() {
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ActiveMQDestination destination = (ActiveMQDestination) session.createQueue(">");
            Destination advisoryTopicFastProducer = AdvisorySupport.getFastProducerAdvisoryTopic(destination);
            LOGGER.info("Subscribing to advisory topic: " + advisoryTopicFastProducer);
            consumer = session.createConsumer(advisoryTopicFastProducer);
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

        DataStructure data = msg.getDataStructure();

        if (data != null && data.getDataStructureType() == ProducerInfo.DATA_STRUCTURE_TYPE) {
            ProducerInfo producerInfo = (ProducerInfo) data;
            logProducerInfo(producerInfo);
        } else {
            LOGGER.warn("Msg:" + msg.toString());
        }

    }

    private void logProducerInfo(ProducerInfo producerInfo) {
        ActiveMQDestination destination = producerInfo.getDestination();

        final String producerId = producerInfo.getProducerId().toString();
        long sentCount = producerInfo.getSentCount();
        int windowSize = producerInfo.getWindowSize();
        String queueName = "";

        if (destination != null) {
            queueName = destination.getPhysicalName();
        }

        LOGGER.warn("Fast producer with producerId:'{}', sentCount:'{}', windowSize:'{}', detected on queue:'{}'",
                producerId,
                sentCount,
                windowSize,
                queueName);
    }
}
