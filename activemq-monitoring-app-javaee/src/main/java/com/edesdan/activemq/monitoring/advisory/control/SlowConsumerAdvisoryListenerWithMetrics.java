package com.edesdan.activemq.monitoring.advisory.control;

import io.prometheus.client.Counter;

import javax.jms.Connection;
import javax.jms.Message;

public class SlowConsumerAdvisoryListenerWithMetrics extends SlowConsumerAdvisoryListener {

    static final Counter slow_consumer_messages_total = Counter.build()
            .name("slow_consumer_messages_total")
            .help("Total messages received for a slow consumer.")
            .register();

    public SlowConsumerAdvisoryListenerWithMetrics(Connection connection) {
        super(connection);
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        slow_consumer_messages_total.inc();
    }
}
