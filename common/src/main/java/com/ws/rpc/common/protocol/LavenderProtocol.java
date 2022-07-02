package com.ws.rpc.common.protocol;

public class LavenderProtocol {
    private long sessionId;
    private byte type;
    private int length;
    private Object body;

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public long getSessionId() {
        return sessionId;
    }

    public byte getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public Object getBody() {
        return body;
    }
}
