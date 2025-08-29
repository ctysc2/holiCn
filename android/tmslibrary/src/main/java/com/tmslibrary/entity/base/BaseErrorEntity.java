package com.tmslibrary.entity.base;

import java.util.ArrayList;

public class BaseErrorEntity extends BaseEntity{

    ArrayList<TenantError> errors;

    String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<TenantError> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<TenantError> errors) {
        this.errors = errors;
    }

}
