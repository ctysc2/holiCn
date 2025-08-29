package com.holimobile.event;

public class SiteContactScanEvent {

    String siteId;
    String userId;

    public SiteContactScanEvent(String siteId, String userId) {
        this.siteId = siteId;
        this.userId = userId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
