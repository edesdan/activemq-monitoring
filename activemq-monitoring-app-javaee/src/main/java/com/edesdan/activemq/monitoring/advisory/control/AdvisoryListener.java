package com.edesdan.activemq.monitoring.advisory.control;

import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageListener;

public abstract class AdvisoryListener implements MessageListener, AutoCloseable {

    transient Connection connection;

    private static Logger LOGGER = LoggerFactory.getLogger(AdvisoryListener.class);


    public AdvisoryListener(Connection connection) {
        this.connection = connection;
    }

    public abstract void start();

    public abstract void close() throws Exception;

    public void onMessage(Message message) {
        ActiveMQMessage msg = (ActiveMQMessage) message;

        LOGGER.warn("Msg:" + msg.toString());

    }
}
