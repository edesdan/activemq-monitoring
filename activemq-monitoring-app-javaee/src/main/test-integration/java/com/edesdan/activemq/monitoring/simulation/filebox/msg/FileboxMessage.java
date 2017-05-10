package com.edesdan.activemq.monitoring.simulation.filebox.msg;

import java.io.Serializable;
import java.util.UUID;

public class FileboxMessage implements Serializable {

    private String msg;

    private String uuid;

    public FileboxMessage(String msg, String uuid) {
        this.msg = msg;
        this.uuid = uuid;
    }

    public FileboxMessage(String msg) {
        this.msg = msg;
        this.uuid = UUID.randomUUID().toString();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "FileboxMessage{" +
                "msg='" + msg + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
