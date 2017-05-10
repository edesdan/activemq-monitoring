package com.edesdan.activemq.monitoring.exceptions.entity;


@SuppressWarnings("WeakerAccess")
public class ActiveMQMonitoringRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ActiveMQMonitoringRuntimeException() {
    }

    public ActiveMQMonitoringRuntimeException(String message) {
        super(message);
    }

    public ActiveMQMonitoringRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActiveMQMonitoringRuntimeException(Throwable cause) {
        super(cause);
    }
}
