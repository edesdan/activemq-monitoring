package com.edesdan.activemq.monitoring.simulation.filebox.listeners;

import com.primeur.ghip.core.GHIPEvent;
import com.primeur.ghip.core.GHIPMessage;
import com.primeur.ghip.core.interfaces.GHIPListener;
import com.primeur.ghip.core.interfaces.GHIPMessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlowResponderServiceListener implements GHIPListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(SlowResponderServiceListener.class);

    private Integer responseDelay = 60000; // default


    public SlowResponderServiceListener() {
    }

    public SlowResponderServiceListener(int delay) {
        this.responseDelay = delay;
    }

    @Override
    public GHIPMessage onMessage(GHIPMessageChannel messageChannel, GHIPMessage message) {
        LOGGER.info("GHIP message received: {}", message);

        try {
            Thread.sleep(responseDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public void onEvent(GHIPEvent event) {

    }
}
