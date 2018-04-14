package com.imnotpayingforthat.imnotpayingforthat.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.adapters.MemberRecyclerAdapter;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;
import com.imnotpayingforthat.imnotpayingforthat.models.User;
import com.imnotpayingforthat.imnotpayingforthat.util.Globals;

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
    private static final String TEAM_NAME_KEY = "TEAMNAME";
    private static final String TEAM_DESC_KEY = "TEAMDESCRIPTION";
    private static final String OWNER_UID_KEY = "OWNERUID";
    private RecyclerView memberList;
    private TextView teamNameTextView, teamDescription;
    private RecyclerView.Adapter adapter;
    private Globals.LayoutManagerType currentLayoutManagerType;
    private RecyclerView.LayoutManager currentLayoutManager;
    private OnTeamDetailsInteractionListener mListener;

    private String teamName;
    private String teamDesc;
    private String ownerUid;

    public TeamDetailsFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static TeamDetailsFragment newInstance(Team team) {
        TeamDetailsFragment fragment = new TeamDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TEAM_NAME_KEY, team.getTeamName());
        bundle.putString(TEAM_DESC_KEY, team.getTeamDescription());
        bundle.putString(OWNER_UID_KEY, team.getOwnerUid());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.teamName = getArguments().getString(TEAM_NAME_KEY);
            this.teamDesc = getArguments().getString(TEAM_DESC_KEY);
            this.ownerUid = getArguments().getString(OWNER_UID_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_team_details, container, false);
        teamNameTextView = v.findViewById(R.id.teamdetail_edittext_teamname);
        teamDescription = v.findViewById(R.id.teamdetail_edittext_teamdescription);
        memberList = v.findViewById(R.id.teamdetail_recyclerview_members);
        v.findViewById(R.id.teamdetail_button_list).setOnClickListener(this);

        Query query = FirebaseFirestore.getInstance()
                .collection("teams")
                .whereEqualTo("teamName", teamName)
                .whereEqualTo("teamDescription", teamDesc)
                .whereEqualTo("ownerUid", ownerUid);
        query.get()
                .addOnSuccessListener(l -> {
                    List<DocumentSnapshot> d = l.getDocuments();
                    if(d.size() >= 1) {
                        DocumentSnapshot doc = d.get(0);
                        setupRecyclerView(doc.getId());
                    }
                })
                .addOnFailureListener(l -> {
                    Log.d("TEST", "OK");
                });

        return v;
    }

    private void setupRecyclerView(String teamId) {

        Query query = FirebaseFirestore.getInstance()
                .collection("users")
                .whereEqualTo("teams." + teamId, true);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();

        adapter = new MemberRecyclerAdapter(options);
        memberList.setHasFixedSize(true);
        memberList.setAdapter(adapter);

        currentLayoutManagerType = Globals.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(currentLayoutManagerType);
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

    public void setRecyclerViewLayoutManager(Globals.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (memberList.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) memberList.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                currentLayoutManager = new GridLayoutManager(getActivity(), 2);
                currentLayoutManagerType = Globals.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                currentLayoutManager = new LinearLayoutManager(getActivity());
                currentLayoutManagerType = Globals.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                currentLayoutManager = new LinearLayoutManager(getActivity());
                currentLayoutManagerType = Globals.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        memberList.setLayoutManager(currentLayoutManager);
        memberList.scrollToPosition(scrollPosition);
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
