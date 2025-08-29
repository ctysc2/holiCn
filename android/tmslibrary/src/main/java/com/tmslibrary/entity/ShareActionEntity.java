package com.tmslibrary.entity;

import java.util.ArrayList;

public class ShareActionEntity {

    DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity{

        String url;
        String title;
        String text;
        String img;
        ArrayList<String> channel;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public ArrayList<String> getChannel() {
            return channel;
        }

        public void setChannel(ArrayList<String> channel) {
            this.channel = channel;
        }
    }
}
