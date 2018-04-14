package com.imnotpayingforthat.imnotpayingforthat.adapters;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.models.User;
import com.imnotpayingforthat.imnotpayingforthat.viewholders.MemberViewHolder;

public class MemberRecyclerAdapter extends FirestoreRecyclerAdapter<User, MemberViewHolder> {

    private static final String TAG = "MemberRecyclerAdapter";
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MemberRecyclerAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MemberViewHolder holder, int position, @NonNull User model) {
        Log.d(TAG, "Setting item: " + position + " - " + model.getFirstName());
        holder.getFirstName().setText(model.getFirstName());
        holder.getLastName().setText(model.getLastName());
        // TODO: 10/04/2018 set team icon
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_team_details_listitem, parent, false);
        return new MemberViewHolder(v);
    }
}
