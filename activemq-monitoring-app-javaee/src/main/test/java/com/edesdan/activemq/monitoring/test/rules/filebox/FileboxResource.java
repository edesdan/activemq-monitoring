package com.edesdan.activemq.monitoring.test.rules.filebox;

import com.edesdan.activemq.monitoring.test.rules.GhipProvider;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;

public class FileboxResource extends ExternalResource{

    @ClassRule
    public static GhipProvider ghipProvider = GhipProvider.createGHIPProvider();

    public static final String FILEBOX_SERVICE_NAME = "FbClientService";

    public static final String FILEBOX_SERVICE_QUEUE_NAME = "primeur.ghip.queue.FbClientService";
}
