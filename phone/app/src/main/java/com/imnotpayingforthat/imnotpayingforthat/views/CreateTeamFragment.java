package com.imnotpayingforthat.imnotpayingforthat.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;
import com.imnotpayingforthat.imnotpayingforthat.repositories.TeamRepository;


public class CreateTeamFragment extends Fragment implements View.OnClickListener {


    private OnCreateTeamFragmentListener mListener;
    private static final String TAG = "CreateTeamFragment";
    private EditText teamNameTextBox;
    private EditText teamDescriptionTextBox;
    private TeamRepository teamRepository;

    public CreateTeamFragment() {
        // Required empty public constructor
    }

    public static CreateTeamFragment newInstance() {
        CreateTeamFragment fragment = new CreateTeamFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_team, container, false);
        teamNameTextBox = view.findViewById(R.id.teamdetail_edittext_teamname);
        teamDescriptionTextBox = view.findViewById(R.id.teamdetail_edittext_teamdescription);
        view.findViewById(R.id.teamdetail_button_cancel).setOnClickListener(this);
        view.findViewById(R.id.teamdetail_button_ok).setOnClickListener(this);
        teamRepository = new TeamRepository();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateTeamFragmentListener) {
            mListener = (OnCreateTeamFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateTeamFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.teamdetail_button_cancel:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.teamdetail_button_ok:
                Team team = new Team(teamNameTextBox.getText().toString(), teamDescriptionTextBox.getText().toString());
                teamRepository.createTeam(team, this::createTeamSuccess, this::createTeamFailure);
                break;
        }
    }

    private void createTeamSuccess(){
        Toast.makeText(this.getContext(), "Created Team", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }
    private void createTeamFailure(){
        Toast.makeText(this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }


    public interface OnCreateTeamFragmentListener {
        void navigateToDeleteTeam();
        void onFragmentInteraction(Uri uri);
    }
}
