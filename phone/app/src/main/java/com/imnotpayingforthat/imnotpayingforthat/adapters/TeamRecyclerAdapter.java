package com.imnotpayingforthat.imnotpayingforthat.adapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.callbacks.TeamRecycleViewHandler;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import java.util.Objects;
import java.util.UUID;
import com.imnotpayingforthat.imnotpayingforthat.viewholders.TeamViewHolder;

public class TeamRecyclerAdapter extends FirestoreRecyclerAdapter<Team, TeamViewHolder> {

    private Context context;
    TeamRecycleViewHandler listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TeamRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Team> options, Context context, TeamRecycleViewHandler listener) {
        super(options);
        this.context = context;
        this.listener = listener;
    }
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onBindViewHolder(@NonNull TeamViewHolder holder, int position, @NonNull Team model) {
        Log.d(TAG, "Setting item: " + position + " - " + model.getTeamName());
        holder.getTeamName().setText(model.getTeamName());
        holder.getTeamDescription().setText(model.getTeamDescription());
        GlideApp.with(context)
                .load("http://thecatapi.com/api/images/get")
                .signature(new ObjectKey(UUID.randomUUID()))
                .into(holder.getTeamIcon());
        // TODO: 10/04/2018 set team icon
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_teamlist_listitem, parent, false);
        TeamViewHolder t = new TeamViewHolder(v, p -> {
            Team team = getItem(p);
            listener.handleTeamItem(team);
            Log.d("RECYCLERADAPTER", team.getTeamName());
        });
        return t;
    }
}
