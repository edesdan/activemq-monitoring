package com.edesdan.activemq.monitoring;

import com.edesdan.activemq.monitoring.simulation.filebox.listeners.EchoServiceListener;
import com.edesdan.activemq.monitoring.simulation.filebox.msg.FileboxMessage;
import com.edesdan.activemq.monitoring.test.rules.filebox.FileboxConsumer;
import com.edesdan.activemq.monitoring.test.rules.filebox.FileboxProducer;
import com.primeur.ghip.core.GHIPMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileboxIT {


    public static final Logger LOGGER = LoggerFactory.getLogger(FileboxIT.class);

    @Rule
    public FileboxProducer fileboxProducer = new FileboxProducer();

    @Rule
    public FileboxConsumer fileboxConsumer = new FileboxConsumer();

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void testFileboxWithEchoServiceWithRequestReply() throws Exception {

        fileboxConsumer.registerListenerOnFileboxServiceQueue(new EchoServiceListener());
        for (int i = 1; i <= 100; i++) {
            GHIPMessage response = fileboxProducer.sendRequestReplyMessageSync(new FileboxMessage("This is a sample request."), 2000);
            Thread.sleep(1000);
        }
        fileboxConsumer.deregisterCurrentListenerOnFileboxServiceQueue();

    }
}
