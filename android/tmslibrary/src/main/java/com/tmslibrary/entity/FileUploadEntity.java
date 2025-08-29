package com.tmslibrary.entity;


import com.tmslibrary.entity.base.BaseEntity;
import com.tmslibrary.entity.base.TenantError;

import java.util.ArrayList;

public class FileUploadEntity extends BaseEntity {

    DataEntity data;

    ArrayList<TenantError> errors;

    public ArrayList<TenantError> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<TenantError> errors) {
        this.errors = errors;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity{
        String id;
        String fileName;
        String previewUrl;
        long fileSize;
        String fileType;

        public long getFileSize() {
            return fileSize;
        }

        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getPreviewUrl() {
            return previewUrl;
        }

        public void setPreviewUrl(String previewUrl) {
            this.previewUrl = previewUrl;
        }
    }
}
