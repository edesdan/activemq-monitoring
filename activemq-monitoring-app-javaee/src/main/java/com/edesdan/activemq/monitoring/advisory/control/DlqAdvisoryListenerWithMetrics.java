package com.edesdan.activemq.monitoring.advisory.control;

import io.prometheus.client.Counter;

import javax.jms.Connection;
import javax.jms.Message;

public class DlqAdvisoryListenerWithMetrics extends DlqAdvisoryListener {

    static final Counter dlq_messages_total = Counter.build()
            .name("dlq_messages_total")
            .help("Total requests of DLQ messages on all queues.")
            .register();


    public DlqAdvisoryListenerWithMetrics(Connection connection) {
        super(connection);
    }

    @Override
    public void onMessage(Message message) {

        super.onMessage(message);
        dlq_messages_total.inc();

    }
}
