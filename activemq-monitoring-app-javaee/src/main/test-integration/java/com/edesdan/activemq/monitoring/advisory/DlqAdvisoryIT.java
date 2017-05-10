package com.edesdan.activemq.monitoring.advisory;

import com.edesdan.activemq.monitoring.simulation.filebox.listeners.SlowResponderServiceListener;
import com.edesdan.activemq.monitoring.simulation.filebox.msg.FileboxMessage;
import com.edesdan.activemq.monitoring.test.rules.filebox.FileboxConsumer;
import com.edesdan.activemq.monitoring.test.rules.filebox.FileboxProducer;
import com.primeur.ghip.core.GHIPMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DlqAdvisoryIT {

    public static final Logger LOGGER = LoggerFactory.getLogger(DlqAdvisoryIT.class);

    @Rule
    public FileboxProducer fileboxProducer = new FileboxProducer();

    @Rule
    public FileboxConsumer fileboxConsumer = new FileboxConsumer();

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void testFileboxDLQWithRequestReply() throws Exception {

        fileboxConsumer.registerListenerOnFileboxServiceQueue(new SlowResponderServiceListener());

        Thread.sleep(10000);
        GHIPMessage response = fileboxProducer.sendRequestReplyMessageSync(new FileboxMessage("This is a sample request."), 1000);

        LOGGER.info("GHIP message reply: {}", response);

        fileboxConsumer.deregisterCurrentListenerOnFileboxServiceQueue();

    }

    @Test
    public void testFileboxDLQWithFireAndForget() throws Exception {

        fileboxConsumer.registerListenerOnFileboxServiceQueue(new SlowResponderServiceListener());

        Thread.sleep(2000);
        fileboxProducer.sendFireAndForgetMsg(new FileboxMessage("This is a sample request."), 1000);

        fileboxConsumer.deregisterCurrentListenerOnFileboxServiceQueue();

    }
}