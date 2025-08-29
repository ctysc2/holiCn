package com.tmslibrary.event;

import javax.inject.Inject;

public class ReFreshTokenEvent {
    String reqType;

    public ReFreshTokenEvent(String reqType) {
        this.reqType = reqType;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }
}
