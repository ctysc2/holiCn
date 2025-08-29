package com.holimobile.event;

public class ReLoginEvent {
    String code;

    public ReLoginEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
