package com.imnotpayingforthat.imnotpayingforthat.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.imnotpayingforthat.imnotpayingforthat.models.ShoppingListItem;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;

public class ShoppingListItemRepository {
    private FirebaseFirestore db;

    public ShoppingListItemRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void deleteShoppingListItem(String teamId, ShoppingListItem item) {
        db.collection(String.format("/teams/%s/shoppingList", teamId))
                .document(item.getId())
                .delete()
                .addOnSuccessListener(l -> {
                    addExpenses(teamId, item.getItemPrice());
                    Log.d("ShoppingRepo", "Success");
                })
                .addOnFailureListener(l -> {
                    Log.d("ShoppingRepo", "Failure");
                });
    }

    public void addExpenses(String teamId, double amount) {
        if(teamId.isEmpty()) {
            return;
        }
        db.document("/teams/"+teamId)
                .get()
                .addOnSuccessListener(l -> {
                    Team t = l.toObject(Team.class);
                    if(t != null) {
                        t.setTotalExpenses(t.getTotalExpenses() + amount);
                    }
                    db.document("/teams/"+teamId)
                            .set(t);
                });
    }
}
