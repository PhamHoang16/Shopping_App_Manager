package com.example.manager.model;

public class ProductCategory {
    int ID;
    String Name;
    String Picture;

    public ProductCategory(String name, String picture) {
        Name = name;
        Picture = picture;
    }

    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }
}
