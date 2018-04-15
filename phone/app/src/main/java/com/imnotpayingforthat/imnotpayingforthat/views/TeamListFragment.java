package com.imnotpayingforthat.imnotpayingforthat.views;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.adapters.MemberRecyclerClickListener;
import com.imnotpayingforthat.imnotpayingforthat.adapters.TeamRecyclerAdapter;
import com.imnotpayingforthat.imnotpayingforthat.adapters.TeamRecyclerClickListener;
import com.imnotpayingforthat.imnotpayingforthat.callbacks.TeamRecycleViewHandler;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;
import com.imnotpayingforthat.imnotpayingforthat.models.User;
import com.imnotpayingforthat.imnotpayingforthat.util.Globals;
import com.imnotpayingforthat.imnotpayingforthat.viewmodels.TeamListViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class TeamListFragment
        extends Fragment
        implements TeamRecycleViewHandler {

    private static final String TAG = "TeamList";

    private TeamListViewModel viewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private OnTeamFragmentInteractionListener mListener;
    private Globals.LayoutManagerType currentLayoutManagerType;
    private RecyclerView.LayoutManager currentLayoutManager;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.teamlistmenu_action_create) {
            Toast.makeText(this.getContext(), "Clicked add team", Toast.LENGTH_LONG).show();
            mListener.navigateToCreateTeam();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.teamlist, menu);
    }

    public TeamListFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
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
        Query query = FirebaseFirestore.getInstance()
                .collection("teams")
                .orderBy("teamName")
                .limit(50);

        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new TeamRecyclerAdapter(options, getContext(), this);

        recyclerView = view.findViewById(R.id.team_recyclerView_teamList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        currentLayoutManagerType = Globals.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
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

    public void setRecyclerViewLayoutManager(Globals.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
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

        recyclerView.setLayoutManager(currentLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void handleTeamItem(Team t) {
        Toast.makeText(getContext(), t.getTeamName(), Toast.LENGTH_SHORT).show();
        mListener.navigateToTeamDetail(t);
    }


    public interface OnTeamFragmentInteractionListener {
        void navigateToCreateTeam();
        void navigateToTeamDetail(Team t);
        void navigateToAddMember(Team t);
    }
}
