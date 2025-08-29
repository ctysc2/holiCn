package com.tmslibrary.entity;

import com.tmslibrary.entity.base.BaseHoLiEntity;

import java.io.Serializable;

public class LoginEntity extends BaseHoLiEntity {

    DataEntity result;

    public DataEntity getResult() {
        return result;
    }

    public void setResult(DataEntity result) {
        this.result = result;
    }

    public static class DataEntity implements Serializable {
        String userName;
        String phone;
        String businessCode;
        String accessToken;
        int uid;
        String deviceLoginId;


        public String getDeviceLoginId() {
            return deviceLoginId;
        }

        public void setDeviceLoginId(String deviceLoginId) {
            this.deviceLoginId = deviceLoginId;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBusinessCode() {
            return businessCode;
        }

        public void setBusinessCode(String businessCode) {
            this.businessCode = businessCode;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }


}
