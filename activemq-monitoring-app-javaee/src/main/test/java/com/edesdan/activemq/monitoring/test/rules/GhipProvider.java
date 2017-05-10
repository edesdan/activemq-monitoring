package com.edesdan.activemq.monitoring.test.rules;


import com.primeur.ghip.core.GHIPException;
import com.primeur.ghip.core.configuration.GHIPConfig;
import com.primeur.ghip.core.configuration.GHIPDispatcherThreadPoolProfile;
import com.primeur.ghip.core.configuration.GHIPEntityProfile;
import com.primeur.ghip.core.interfaces.GHIPClient;
import com.primeur.ghip.core.interfaces.GHIPConnection;
import com.primeur.ghip.core.interfaces.GHIPConnectionFactory;
import com.primeur.ghip.core.interfaces.GHIPListener;
import com.primeur.ghip.core.interfaces.GHIPPublisher;
import com.primeur.ghip.core.interfaces.GHIPService;
import com.primeur.ghip.core.interfaces.GHIPSubscriber;
import com.primeur.ghip.core.jms.amq.AMQFailoverProfile;
import com.primeur.ghip.core.jms.amq.AMQPoolProfile;
import com.primeur.ghip.core.jms.amq.AMQProfile;
import com.primeur.ghip.core.jms.amq.GHIPAMQConnectionFactory;
import org.junit.rules.ExternalResource;


public class GhipProvider extends ExternalResource {

    private static final int GHIP_CONNECTION_DEFAULT_TIMEOUT = 10000;
    private static final String ACTIVEMQ_BROKER_DEFAULT_HOST = "localhost";
    private static final int ACTIVEMQ_BROKER_DEFAULT_PORT = 61616;
    private static final int GHIP_CLIENT_DEFAULT_REPLY_TIMEOUT = 10000;
    private static final int GHIP_CLIENT_DEFAULT_MSG_TTL = 10000;
    private static final int GHIP_CLIENT_DEFAULT_MSG_MAX_RETRY_NUMBER = 0;

    private GHIPConfig ghipConfig;
    private GHIPConnectionFactory ghipConnectionFactory;
    private GHIPConnection ghipConnection;


    private GhipProvider(GHIPConfig ghipConfig) {

        this.ghipConfig = ghipConfig;

        try {
            ghipConnectionFactory = new GHIPAMQConnectionFactory(ghipConfig);
        } catch (GHIPException e) {
            e.printStackTrace();
        }

        try {
            ghipConnection = ghipConnectionFactory.createConnection(GHIP_CONNECTION_DEFAULT_TIMEOUT);
        } catch (GHIPException e) {
            e.printStackTrace();
        }
    }


    public static GhipProvider createGHIPProvider(String brokerHost, int brokerPort) {

        GHIPConfig ghipConfig = buildGhipConfig(brokerHost, brokerPort);

        return new GhipProvider(ghipConfig);
    }

    private static GHIPConfig buildGhipConfig(String brokerHost, int brokerPort) {

        GHIPConfig ghipConfig = new GHIPConfig();
        ghipConfig.setBrokerServers("tcp://" + brokerHost);
        ghipConfig.setBrokerPorts(brokerPort);

         /*
         * protocol specific configuration : currently we only support ActiveMQ as an implementation of JMS
         */
        AMQFailoverProfile amqFailoverProfile = AMQFailoverProfile.getDefaultProfile();
        AMQPoolProfile amqPoolProfile = AMQPoolProfile
                .createProfile()
                .maxConnection(50)
                .maxSession(500);
        AMQProfile amqProfile = AMQProfile
                .createProfile()
                .setFailoverProfile(amqFailoverProfile)
                .setPoolProfile(amqPoolProfile);
        /*
         *  thread pool configuration used by ghip reactor. it is same as thread pool configuration in JDK
         */
        GHIPDispatcherThreadPoolProfile threadPoolProfile = GHIPDispatcherThreadPoolProfile
                .createProfile()
                .setCorePoolSize(30)
                .setMaxPoolSize(50);
        ghipConfig.setGHIPThreadPoolProfile(threadPoolProfile);
        ghipConfig.setVendorProfile(amqProfile);

        return ghipConfig;
    }


    public static GhipProvider createGHIPProvider() {

        return createGHIPProvider(ACTIVEMQ_BROKER_DEFAULT_HOST, ACTIVEMQ_BROKER_DEFAULT_PORT);
    }

    /**
     * Create and then register service.
     * For registration you need to pass a class that implements GHIPListener interface.
     *
     * @param listener    class used for service registration (must implements GHIPListener interface).
     * @param implicitAck false means user must invoke ack() method on channel manually.
     * @return a GHIPService with a listener registered on it.
     * @throws Exception
     */
    public GHIPService createGhipService(GHIPListener listener, String serviceName, boolean implicitAck) throws Exception {

        GHIPService ghipService = ghipConnection.createService();
        ghipService.setImplicitAck(implicitAck)
                .setListener(listener)
                .registerTo(serviceName);


        return ghipService;
    }

    /**
     * Create a GHIP client with default entity profile.
     *
     * @return
     * @throws Exception
     */
    public GHIPClient createGhipClient() throws Exception {
        /*
         * We specify GHIPEntityConfig
         * with defaults values that can be later overridden
         */
        GHIPEntityProfile entityProfile = GHIPEntityProfile
                .createGHIPEntityProfile()
                .setReplyTimeout(GHIP_CLIENT_DEFAULT_REPLY_TIMEOUT)
                .setMessageTTL(GHIP_CLIENT_DEFAULT_MSG_TTL)
                .setRetryNumber(GHIP_CLIENT_DEFAULT_MSG_MAX_RETRY_NUMBER);

        return ghipConnection.createClient(entityProfile);

    }

    public GHIPPublisher createGhipPublisher() throws Exception {

        throw new UnsupportedOperationException();
    }

    public GHIPSubscriber createGhipSubscriber() throws Exception {

        throw new UnsupportedOperationException();
    }


    @Override
    protected void before() throws Throwable {
    }

    @Override
    protected void after() {
    }

}
