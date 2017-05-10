package com.edesdan.activemq.monitoring.advisory.control;

import io.prometheus.client.Counter;

import javax.jms.Connection;
import javax.jms.Message;

public class FastProducerAdvisoryListenerWithMetrics extends FastProducerAdvisoryListener {

    static final Counter fast_producer_messages_total = Counter.build()
            .name("fast_producer_messages_total")
            .help("Total messages received for a fast producer.")
            .register();


    public FastProducerAdvisoryListenerWithMetrics(Connection connection) {
        super(connection);
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);

        fast_producer_messages_total.inc();

    }
}
