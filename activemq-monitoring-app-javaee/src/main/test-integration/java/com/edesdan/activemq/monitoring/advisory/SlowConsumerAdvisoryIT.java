package com.edesdan.activemq.monitoring.advisory;

import com.edesdan.activemq.monitoring.simulation.filebox.listeners.SlowResponderServiceListener;
import com.edesdan.activemq.monitoring.simulation.filebox.msg.FileboxMessage;
import com.edesdan.activemq.monitoring.test.rules.filebox.FileboxConsumer;
import com.edesdan.activemq.monitoring.test.rules.filebox.FileboxProducer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlowConsumerAdvisoryIT {


    public static final Logger LOGGER = LoggerFactory.getLogger(SlowConsumerAdvisoryIT.class);

    @Rule
    public FileboxProducer fileboxProducer = new FileboxProducer();

    @Rule
    public FileboxConsumer fileboxConsumer = new FileboxConsumer();

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void testFileboxSlowConsumer() throws Exception {

        int responseDelay = 10 * 1000; // Consumer is ten time slower then the producer
        int producerDelay = 1 * 25;

        fileboxConsumer.registerListenerOnFileboxServiceQueue(new SlowResponderServiceListener(responseDelay));


        for (int i = 1; i <= 10000; i++) {

            fileboxProducer.sendFireAndForgetMsg(new FileboxMessage("This is a sample request."), 120000);
            LOGGER.info("Sending message: {}", i);

            Thread.sleep(producerDelay);

        }

        fileboxConsumer.deregisterCurrentListenerOnFileboxServiceQueue();

    }
}
