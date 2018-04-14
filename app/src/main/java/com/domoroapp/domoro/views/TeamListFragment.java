package com.domoroapp.domoro.views;

import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.Toast;

import com.domoroapp.domoro.R;
import com.domoroapp.domoro.adapters.TeamRecyclerAdapter;
import com.domoroapp.domoro.models.Team;
import com.domoroapp.domoro.viewModels.TeamListViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class TeamListFragment extends Fragment {

    private static final String TAG = "TeamList";

    private TeamListViewModel viewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private OnTeamFragmentInteractionListener mListener;
    private LayoutManagerType currentLayoutManagerType;
    private RecyclerView.LayoutManager currentLayoutManager;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public TeamListFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static TeamListFragment newInstance() {
        TeamListFragment fragment = new TeamListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TeamListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teamlist, container, false);
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String memberPath = String.format("members.%s", currentUserUid);
        Query query = FirebaseFirestore.getInstance()
                .collection("teams")
                .orderBy("teamName")
                .whereEqualTo(memberPath, true)
                .limit(50);

        query.get().addOnSuccessListener(l -> {
           List<Team> t = l.toObjects(Team.class);
           for(Team x : t) {
               Log.d(TAG, x.getTeamName());
           }
        })
        .addOnFailureListener(f -> {
            Log.d(TAG, f.toString());
        });

        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new TeamRecyclerAdapter(options);

        recyclerView = view.findViewById(R.id.team_recyclerView_teamList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(currentLayoutManagerType);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTeamFragmentInteractionListener) {
            mListener = (OnTeamFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTeamFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                currentLayoutManager = new GridLayoutManager(getActivity(), 2);
                currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                currentLayoutManager = new LinearLayoutManager(getActivity());
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                currentLayoutManager = new LinearLayoutManager(getActivity());
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(currentLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    public interface OnTeamFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
