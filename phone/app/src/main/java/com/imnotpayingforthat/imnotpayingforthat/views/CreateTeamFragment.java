package com.imnotpayingforthat.imnotpayingforthat.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imnotpayingforthat.imnotpayingforthat.R;


public class CreateTeamFragment extends Fragment {


    private OnCreateTeamFragmentListener mListener;

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
        return inflater.inflate(R.layout.fragment_create_team, container, false);
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

    public interface OnCreateTeamFragmentListener {
        void navigateToDeleteTeaem();
        void onFragmentInteraction(Uri uri);
    }
}
