package com.edesdan.activemq.monitoring.simulation.filebox.listeners;

import com.primeur.ghip.core.GHIPEvent;
import com.primeur.ghip.core.GHIPMessage;
import com.primeur.ghip.core.interfaces.GHIPListener;
import com.primeur.ghip.core.interfaces.GHIPMessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServiceListener implements GHIPListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(EchoServiceListener.class);

    @Override
    public GHIPMessage onMessage(GHIPMessageChannel messageChannel, GHIPMessage message) {
        LOGGER.info("GHIP message received: {}", message);
        return message;
    }

    @Override
    public void onEvent(GHIPEvent event) {

    }
}
