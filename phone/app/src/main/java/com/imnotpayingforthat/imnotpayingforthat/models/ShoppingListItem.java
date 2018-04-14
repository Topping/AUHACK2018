package com.imnotpayingforthat.imnotpayingforthat.models;

public class ShoppingListItem {
    private String itemName;
    private Double price;

    public ShoppingListItem(String itemName) {
        this.itemName = itemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
