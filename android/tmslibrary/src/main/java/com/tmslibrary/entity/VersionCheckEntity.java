package com.tmslibrary.entity;

import com.tmslibrary.entity.base.BaseHoLiEntity;

public class VersionCheckEntity extends BaseHoLiEntity {

    DataEntity result;

    public DataEntity getResult() {
        return result;
    }

    public void setResult(DataEntity result) {
        this.result = result;
    }

    public static class DataEntity{
        String updateFlag;
        int foreceUpdate;
        String updateUrl;

        String versionCode;

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getUpdateFlag() {
            return updateFlag;
        }

        public void setUpdateFlag(String updateFlag) {
            this.updateFlag = updateFlag;
        }

        public int getForeceUpdate() {
            return foreceUpdate;
        }

        public void setForeceUpdate(int foreceUpdate) {
            this.foreceUpdate = foreceUpdate;
        }

        public String getUpdateUrl() {
            return updateUrl;
        }

        public void setUpdateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
        }
    }
}
