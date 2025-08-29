package com.tmslibrary.entity;


import com.tmslibrary.entity.base.BaseErrorEntity;

public class DocConfigDetailEntity extends BaseErrorEntity {

    DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity{
        String id;
        String status;
        String docmentEn;
        String docmentCn;
        String docVersion;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDocmentEn() {
            return docmentEn;
        }

        public void setDocmentEn(String docmentEn) {
            this.docmentEn = docmentEn;
        }

        public String getDocmentCn() {
            return docmentCn;
        }

        public void setDocmentCn(String docmentCn) {
            this.docmentCn = docmentCn;
        }

        public String getDocVersion() {
            return docVersion;
        }

        public void setDocVersion(String docVersion) {
            this.docVersion = docVersion;
        }
    }
}
