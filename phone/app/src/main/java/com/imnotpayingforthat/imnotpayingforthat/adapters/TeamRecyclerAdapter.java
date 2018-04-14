package com.imnotpayingforthat.imnotpayingforthat.adapters;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamRecyclerAdapter extends FirestoreRecyclerAdapter<Team, TeamRecyclerAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TeamRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Team> options) {
        super(options);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView teamName, teamDescription;
        private CircleImageView teamIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            teamName = itemView.findViewById(R.id.team_textView_teamName);
            teamDescription = itemView.findViewById(R.id.team_textView_teamDescription);
            teamIcon = itemView.findViewById(R.id.team_imageview_icon);
        }

        public TextView getTeamName() {
            return teamName;
        }

        public TextView getTeamDescription() {
            return teamDescription;
        }

        public CircleImageView getTeamIcon() {
            return teamIcon;
        }
    }
    private final String TAG = this.getClass().getSimpleName();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_teamlist_listitem, parent, false);

        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Team model) {
        Log.d(TAG, "Setting item: " + position + " - " + model.getTeamName());
        holder.getTeamName().setText(model.getTeamName());
        holder.getTeamDescription().setText(model.getTeamDescription());
        // TODO: 10/04/2018 set team icon
    }
}
