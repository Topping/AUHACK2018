package com.imnotpayingforthat.imnotpayingforthat.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.imnotpayingforthat.imnotpayingforthat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberViewHolder extends RecyclerView.ViewHolder {

    private TextView firstName, lastName;
    private CircleImageView profilePicture;

    public TextView getFirstName() {
        return firstName;
    }

    public TextView getLastName() {
        return lastName;
    }

    public CircleImageView getProfilePicture() {
        return profilePicture;
    }

    public MemberViewHolder(View itemView) {
        super(itemView);
        firstName = itemView.findViewById(R.id.teamdetail_textview_firstname);
        lastName = itemView.findViewById(R.id.teamdetail_textview_lastname);
        profilePicture = itemView.findViewById(R.id.teamdetail_imageveiw_profilepic);
    }


}
