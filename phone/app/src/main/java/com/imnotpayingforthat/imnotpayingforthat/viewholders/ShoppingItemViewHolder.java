package com.imnotpayingforthat.imnotpayingforthat.viewholders;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.adapters.ShoppingListRecyclerListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShoppingItemViewHolder extends RecyclerView.ViewHolder {

    private TextView itemName, itemPrice;

    public ShoppingItemViewHolder(View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.textView_name);
        itemPrice = itemView.findViewById(R.id.textView_price);
    }

    public TextView getItemName() {
        return itemName;
    }

    public TextView getItemPrice() {
        return itemPrice;
    }

    public int getThingPosition() {
        return getAdapterPosition();
    }

}
