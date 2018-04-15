package com.imnotpayingforthat.imnotpayingforthat.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

public class ShoppingListItemRepository {
    private FirebaseFirestore db;

    public ShoppingListItemRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void deleteShoppingListItem(String teamId, String shoppingItemId) {
        db.collection(String.format("/teams/%s/shoppingList", teamId))
                .document(shoppingItemId)
                .delete()
                .addOnSuccessListener(l -> {
                    Log.d("ShoppingRepo", "Success");
                })
                .addOnFailureListener(l -> {
                    Log.d("ShoppingRepo", "Failure");
                });
    }
}
