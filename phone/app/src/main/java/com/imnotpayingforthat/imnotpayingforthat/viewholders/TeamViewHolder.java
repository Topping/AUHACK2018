package com.imnotpayingforthat.imnotpayingforthat.viewholders;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.adapters.TeamRecyclerClickListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView teamName, teamDescription;
    private CircleImageView teamIcon;
    private TeamRecyclerClickListener listener;

    public TeamViewHolder(View itemView, TeamRecyclerClickListener listener) {
        super(itemView);
        itemView.findViewById(R.id.teamlist_constraintlayout_background).setOnClickListener(this);
        teamName = itemView.findViewById(R.id.team_textView_teamName);
        teamDescription = itemView.findViewById(R.id.team_textView_teamDescription);
        teamIcon = itemView.findViewById(R.id.team_imageveiw_profilepic);
        this.listener = listener;
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.teamlist_constraintlayout_background) {
            listener.teamItemClicked(getAdapterPosition());
        }
    }
}
