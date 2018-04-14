package com.imnotpayingforthat.imnotpayingforthat.models;

public class ShoppingListItem {
    private String itemName;
    private Double itemPrice;

    public ShoppingListItem(String itemName) {
        this.itemName = itemName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
