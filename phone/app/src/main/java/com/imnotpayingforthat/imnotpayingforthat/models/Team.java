package com.imnotpayingforthat.imnotpayingforthat.models;

import android.net.Uri;

import java.util.HashMap;
import java.util.List;

public class Team {
    private String teamName;
    private String teamDescription;
    private String ownerUid;
    private List<String> members;
    private String id;
    private Uri iconUri;
    private List<ShoppingListItem> shoppingList;
    private double totalExpenses;

    public Team() {}

    public Team(String teamName, String teamDescription) {
        this.teamName = teamName;
        this.teamDescription = teamDescription;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String uid) {
        ownerUid = uid;
    }

    public List<ShoppingListItem> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<ShoppingListItem> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public Uri getIconUri() {
        return iconUri;
    }

    public void setIconUri(Uri iconUri) {
        this.iconUri = iconUri;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
}
