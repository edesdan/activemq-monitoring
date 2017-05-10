package com.edesdan.activemq.monitoring.test.rules;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.BlockingQueue;

public class ActiveMQConsumer extends ExternalResource implements MessageListener, ExceptionListener {

    private static final String EMBEDDED_BROKER_HOST = "localhost";

    private static final Integer EMBEDDED_BROKER_PORT = 61617;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQConsumer.class);

    private final BlockingQueue<TextMessage> messageQueue;
    private Connection connection;
    private Session session;
    private final String queueName;

    public ActiveMQConsumer(BlockingQueue<TextMessage> messageQueue,
                            String queueName) throws JMSException {

        this.messageQueue = messageQueue;
        this.queueName = queueName;
    }

    @Override
    protected void before() throws Throwable {
        connect();
    }

    public void connect() throws JMSException {
        LOGGER.debug("Connecting activeMq consumer...");
        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://" + EMBEDDED_BROKER_HOST + ":" + EMBEDDED_BROKER_PORT.toString());

        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.start();

        connection.setExceptionListener(this);

        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue(queueName);

        // Create a MessageConsumer from the Session to the Topic or Queue
        MessageConsumer consumer = session.createConsumer(destination);

        consumer.setMessageListener(this);
    }

    @Override
    protected void after() {
        LOGGER.debug("closing activemq consumer...");
        try {
            connection.close();
            session.close();

            LOGGER.info("activemq consumer successfully closed");
        } catch (JMSException e) {
            LOGGER.warn("cannot gracefully close JMS connection and session", e);
        }
    }




    public BlockingQueue<TextMessage> getMessageQueue() {
        return messageQueue;
    }

    @Override
    public void onMessage(Message message) {


        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text;
            try {
                text = textMessage.getText();
                messageQueue.add(textMessage);
            } catch (JMSException e) {
                throw new RuntimeException("onMessage method called, something went wrong: " + e.getMessage());
            }
            LOGGER.info("Received message instanceof TextMessage: " + text);
        } else {
            LOGGER.info("Received: " + message);
        }

    }


    @Override
    public void onException(JMSException exception) {
        LOGGER.warn("Something happened with the connection. Exception: " + exception);
    }
}
