package com.edesdan.activemq.monitoring.test.rules.filebox;

import com.primeur.ghip.core.GHIPBuilders;
import com.primeur.ghip.core.GHIPException;
import com.primeur.ghip.core.GHIPMessage;
import com.primeur.ghip.core.interfaces.GHIPClient;

import java.io.Serializable;

public class FileboxProducer extends FileboxResource {

    private GHIPClient ghipClient;


    public GHIPMessage sendRequestReplyMessageSync(Serializable message, long ttl) throws GHIPException {

        GHIPMessage ghipMessage = GHIPBuilders.buildMessage().
                withBody(message)
                .build();

        GHIPMessage reply = ghipClient.createRequestReply()
                .withMessage(ghipMessage)
                .withTimeToLive(ttl)
                .sendTo(FILEBOX_SERVICE_NAME);

        return reply;
    }

    public void sendFireAndForgetMsg(Serializable message, long ttl) throws GHIPException {

        GHIPMessage ghipMessage = GHIPBuilders.buildMessage().
                withBody(message)
                .build();

        ghipClient.createRequestOnly()
                .withMessage(ghipMessage)
                .withTimeToLive(ttl)
                .fireTo(FILEBOX_SERVICE_NAME);

    }


    @Override
    protected void before() throws Throwable {
        super.before();
        try {
            ghipClient = ghipProvider.createGhipClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void after() {
        try {
            ghipClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.after();
    }
}
