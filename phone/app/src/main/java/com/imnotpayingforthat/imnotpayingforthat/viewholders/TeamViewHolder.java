package com.imnotpayingforthat.imnotpayingforthat.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.imnotpayingforthat.imnotpayingforthat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamViewHolder extends RecyclerView.ViewHolder {

    private TextView teamName, teamDescription;
    private CircleImageView teamIcon;

    public TeamViewHolder(View itemView) {
        super(itemView);

        teamName = itemView.findViewById(R.id.team_textView_teamName);
        teamDescription = itemView.findViewById(R.id.team_textView_teamDescription);
        teamIcon = itemView.findViewById(R.id.team_imageveiw_profilepic);
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
