package com.tmslibrary.entity;

import com.tmslibrary.entity.base.BaseHoLiEntity;

import java.io.Serializable;

public class VerifyCodeEntity extends BaseHoLiEntity {

    DataEntity result;

    public DataEntity getResult() {
        return result;
    }

    public void setResult(DataEntity result) {
        this.result = result;
    }

    public static class DataEntity implements Serializable {
        int userId;
        String password;
        String phone;
        String captcha;
        String userName;
        String businessCode;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCaptcha() {
            return captcha;
        }

        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getBusinessCode() {
            return businessCode;
        }

        public void setBusinessCode(String businessCode) {
            this.businessCode = businessCode;
        }
    }


}
