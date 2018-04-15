package com.imnotpayingforthat.imnotpayingforthat.adapters;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.callbacks.ShoppingListRecycleViewHandler;
import com.imnotpayingforthat.imnotpayingforthat.models.ShoppingListItem;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.imnotpayingforthat.imnotpayingforthat.util.Globals;
import com.imnotpayingforthat.imnotpayingforthat.viewholders.ShoppingItemViewHolder;

import java.util.Random;

public class ShoppingItemRecyclerAdapter extends FirestoreRecyclerAdapter<ShoppingListItem, ShoppingItemViewHolder> {

    private Context context;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ShoppingItemRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ShoppingListItem> options) {
        super(options);
    }
    private final String TAG = this.getClass().getSimpleName();

    @NonNull
    @Override
    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_shoppinglist_listitem, parent, false);
        v.setBackgroundColor(Globals.getNextColor());
        return new ShoppingItemViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, int position, @NonNull ShoppingListItem model) {
        Log.d(TAG, "Setting item: " + position + " - " + model.getItemName());
        holder.getItemName().setText(model.getItemName());
        holder.getItemPrice().setText(model.getItemPrice().toString());
        }

    public ShoppingListItem getItemAt(int position) {
        return getItem(position);
    }
}
