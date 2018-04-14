package com.imnotpayingforthat.imnotpayingforthat.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;
import com.imnotpayingforthat.imnotpayingforthat.models.User;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTeamDetailsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "TeamDetailsFragment";
    private RecyclerView memberList;
    private TextView teamName, teamDescription;
    private String teamId;
    private OnTeamDetailsInteractionListener mListener;

    public TeamDetailsFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static TeamDetailsFragment newInstance(String teamId) {
        TeamDetailsFragment fragment = new TeamDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("teamId", teamId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.teamId = getArguments().getString("teamId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_team_details, container, false);
        teamName = v.findViewById(R.id.teamdetail_edittext_teamname);
        teamDescription = v.findViewById(R.id.teamdetail_edittext_teamdescription);
        memberList = v.findViewById(R.id.teamdetail_recyclerview_members);

//        Query query =
        FirebaseFirestore.getInstance()
                .collection("teams")
                .document(teamId)
                .collection("members")
                .get().addOnSuccessListener(l -> {
            List<User> x = l.toObjects(User.class);
            Log.d(TAG, "Get success");
        }).addOnFailureListener(l -> {
            Log.d(TAG, "Get fail");
        });
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.teamdetail_button_list:
                mListener.navigateToShoppingList();
               break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTeamDetailsInteractionListener) {
            mListener = (OnTeamDetailsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTeamDetailsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTeamDetailsInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void navigateToShoppingList();
    }
}
