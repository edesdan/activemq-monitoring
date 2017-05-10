package com.edesdan.activemq.monitoring;


import com.edesdan.activemq.monitoring.advisory.boundary.Advisory;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class ActiveMqMonitoringApplication {

    @Inject
    transient Logger logger;

    @Inject
    Advisory advisory;


    void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

        // ensure beans are initialized
        advisory.start();
    }

    void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
    }

}
