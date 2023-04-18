package com.example.manager.model.EventBus;

import com.example.manager.adapter.NewProductAdapter;
import com.example.manager.model.NewProduct;

public class SuaXoaEvent {
    NewProduct newProduct;

    public SuaXoaEvent(NewProduct newProduct) {
        this.newProduct = newProduct;
    }

    public NewProduct getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(NewProduct newProduct) {
        this.newProduct = newProduct;
    }
}
