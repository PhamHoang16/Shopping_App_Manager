package com.example.manager.model.EventBus;

import com.example.manager.model.Order;

public class OrderEvent {
    Order order;

    public OrderEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
