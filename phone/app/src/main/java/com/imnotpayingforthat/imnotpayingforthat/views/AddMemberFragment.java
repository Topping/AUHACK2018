package com.imnotpayingforthat.imnotpayingforthat.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imnotpayingforthat.imnotpayingforthat.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddMemberFragment.OnAddMemberListener} interface
 * to handle interaction events.
 * Use the {@link AddMemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMemberFragment extends Fragment {
    private OnAddMemberListener mListener;

    public AddMemberFragment() {
        // Required empty public constructor
    }


    public static AddMemberFragment newInstance() {
        AddMemberFragment fragment = new AddMemberFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_add_member, container, false);
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
        if (context instanceof OnAddMemberListener) {
            mListener = (OnAddMemberListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddMemberListener");
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
    public interface OnAddMemberListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
