package com.edesdan.activemq.monitoring.advisory.boundary;

import com.edesdan.activemq.monitoring.advisory.control.AdvisoryListener;
import com.edesdan.activemq.monitoring.advisory.control.DlqAdvisoryListenerWithMetrics;
import com.edesdan.activemq.monitoring.advisory.control.FastProducerAdvisoryListenerWithMetrics;
import com.edesdan.activemq.monitoring.advisory.control.SlowConsumerAdvisoryListenerWithMetrics;
import com.edesdan.activemq.monitoring.exceptions.entity.ActiveMQMonitoringRuntimeException;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class Advisory {

    @Resource(lookup = "java:/activemq/ConnectionFactory")
    transient ConnectionFactory connectionFactory;

    @Inject
    transient Logger logger;

    transient Connection connection;

    private List<AdvisoryListener> advisoryListeners;


    @PostConstruct
    void init() {

        try {
            advisoryListeners = new ArrayList<>();
            initAdvisoryConsumers();

        } catch (Exception e) {
            throw new ActiveMQMonitoringRuntimeException("error during initialization of the activemq monitoring application", e);
        }
    }

    /**
     * Each consumer
     * should create its own sessions from a given connection and sessions
     * should not be shared among consumers.
     */
    private void initAdvisoryConsumers() throws JMSException {

        logger.info("activemq monitoring application will now initialize all the advisory consumers configured.");

        connection = connectionFactory.createConnection();
        connection.start();

        AdvisoryListener dlqAdvisoryListener = null;
        AdvisoryListener slowConsumerAdvisoryListener = null;
        AdvisoryListener fastProducerAdvisoryListener = null;


            dlqAdvisoryListener = new DlqAdvisoryListenerWithMetrics(connection);
            slowConsumerAdvisoryListener = new SlowConsumerAdvisoryListenerWithMetrics(connection);
            fastProducerAdvisoryListener = new FastProducerAdvisoryListenerWithMetrics(connection);


        advisoryListeners.add(dlqAdvisoryListener);
        advisoryListeners.add(slowConsumerAdvisoryListener);
        advisoryListeners.add(fastProducerAdvisoryListener);

        for (AdvisoryListener listener : advisoryListeners) {
            listener.start();
        }

    }


    @PreDestroy
    void shutdown() {

        try {
            for (AdvisoryListener listener : advisoryListeners) {
                listener.close();
            }
            connection.close();
        } catch (Exception e) {
            throw new ActiveMQMonitoringRuntimeException("error during shutdown of the activemq monitoring application", e);
        }
    }

    /**
     * Dummy method to ensure bean initialization at startup
     */
    public void start() {
    }


}
