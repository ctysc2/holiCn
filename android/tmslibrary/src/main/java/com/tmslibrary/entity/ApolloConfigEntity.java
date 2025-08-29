package com.tmslibrary.entity;


import com.tmslibrary.entity.base.BaseEntity;
import com.tmslibrary.entity.base.TenantError;

import java.util.ArrayList;

public class ApolloConfigEntity extends BaseEntity {

    ArrayList<DataEntity> data;

    ArrayList<TenantError> errors;

    public ArrayList<TenantError> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<TenantError> errors) {
        this.errors = errors;
    }

    public ArrayList<DataEntity> getData() {
        return data;
    }

    public void setData(ArrayList<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity{

        String namespaceName;
        ArrayList<Item> items;

        public String getNamespaceName() {
            return namespaceName;
        }

        public void setNamespaceName(String namespaceName) {
            this.namespaceName = namespaceName;
        }

        public ArrayList<Item> getItems() {
            return items;
        }

        public void setItems(ArrayList<Item> items) {
            this.items = items;
        }

        public static class Item{
            String key;
            String value;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
