package com.edesdan.activemq.monitoring.test.rules.filebox;

import com.primeur.ghip.core.interfaces.GHIPListener;
import com.primeur.ghip.core.interfaces.GHIPService;

public class FileboxConsumer extends FileboxResource {

    private GHIPService ghipService;


    public void registerListenerOnFileboxServiceQueue(GHIPListener listener) throws Exception {

        ghipService = ghipProvider.createGhipService(listener, FILEBOX_SERVICE_NAME, true);

    }

    public void deregisterCurrentListenerOnFileboxServiceQueue() throws Exception {

        ghipService.close();

    }


    @Override
    protected void before() throws Throwable {

    }

    @Override
    protected void after() {
        try {
            ghipService.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.after();
    }

}
