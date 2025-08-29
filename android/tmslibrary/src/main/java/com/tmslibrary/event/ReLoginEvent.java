package com.tmslibrary.event;

public class ReLoginEvent {
    int code;

    public ReLoginEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
