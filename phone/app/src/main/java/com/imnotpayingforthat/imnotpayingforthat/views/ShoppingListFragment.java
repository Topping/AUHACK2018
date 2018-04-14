package com.imnotpayingforthat.imnotpayingforthat.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.adapters.MemberRecyclerAdapter;
import com.imnotpayingforthat.imnotpayingforthat.adapters.ShoppingItemRecyclerAdapter;
import com.imnotpayingforthat.imnotpayingforthat.models.ShoppingListItem;
import com.imnotpayingforthat.imnotpayingforthat.models.User;
import com.imnotpayingforthat.imnotpayingforthat.util.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnShoppingListInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ShoppingListFragment";
    private EditText itemNameEditText, itemPriceEditText;
    private OnShoppingListInteractionListener mListener;
    private RecyclerView itemList;
    private String teamId;
    private RecyclerView.Adapter adapter;
    private Globals.LayoutManagerType currentLayoutManagerType;
    private RecyclerView.LayoutManager currentLayoutManager;
    private Query query;

    public ShoppingListFragment() {
        // Required empty public constructor
    }


    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.teamId = getArguments().getString("teamId");
        }
    }

    private void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                // TODO: 15-04-2018 GET THAT ID! AND DELETE AT THIS POSITION!
                Toast.makeText(getActivity(),String.valueOf(swipedPosition), Toast.LENGTH_SHORT).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(itemList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        itemList = v.findViewById(R.id.shoppinglist_recyclerview_list);
        v.findViewById(R.id.shoppinglist_button_additem).setOnClickListener(this);
        setupRecyclerview();
        itemPriceEditText = v.findViewById(R.id.shoppinglist_edittext_itemprice);
        itemNameEditText = v.findViewById(R.id.shoppinglist_edittext_itemname);
        setupItemTouchHelper();
        return v;
    }

    private void setupRecyclerview() {
        query = FirebaseFirestore.getInstance()
                    .collection("teams")
                    .document(teamId)
                    .collection("shoppingList")
                    .orderBy("itemPrice", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ShoppingListItem> options = new FirestoreRecyclerOptions.Builder<ShoppingListItem>()
                .setQuery(query, ShoppingListItem.class)
                .setLifecycleOwner(this)
                .build();


        adapter = new ShoppingItemRecyclerAdapter(options);
        itemList.setHasFixedSize(true);
        itemList.setAdapter(adapter);

        currentLayoutManagerType = Globals.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(currentLayoutManagerType);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShoppingListInteractionListener) {
            mListener = (OnShoppingListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShoppingListInteractionListener");
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
        if (itemList.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) itemList.getLayoutManager())
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

        itemList.setLayoutManager(currentLayoutManager);
        itemList.scrollToPosition(scrollPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shoppinglist_button_additem:
                try {
                    String itemName = itemNameEditText.getText().toString();
                    if(itemName.isEmpty()) {
                        //HMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM😲😲😲😲😲😲
                        throw new NullPointerException();
                    }
                    double itemPrice = Double.parseDouble(itemPriceEditText.getText().toString());
                    ShoppingListItem item = new ShoppingListItem(itemName, itemPrice);
                    FirebaseFirestore.getInstance()
                            .collection("teams")
                            .document(teamId)
                            .collection("shoppingList")
                            .add(item)
                    .addOnSuccessListener(l -> {
                        Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                        itemNameEditText.setText("");
                        itemPriceEditText.setText("");
                    })
                    .addOnFailureListener(l -> {
                        Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    Toast.makeText(getContext(), "That price is not a number", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
    public interface OnShoppingListInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
