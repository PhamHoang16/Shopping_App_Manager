package com.example.manager.model;

import java.util.List;

public class ProductCategoryModel {
    boolean success;
    String message;
    List<ProductCategory> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProductCategory> getResult() {
        return result;
    }

    public void setResult(List<ProductCategory> result) {
        this.result = result;
    }
}
