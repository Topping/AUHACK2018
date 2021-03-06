package com.imnotpayingforthat.imnotpayingforthat.models;

public class ShoppingListItem {
    private String itemName;
    private Double itemPrice;
    private String id;

    public ShoppingListItem() {}

    public ShoppingListItem(String itemName, double itemPrice) {

        this.itemName = itemName;
        this.itemPrice = itemPrice;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
