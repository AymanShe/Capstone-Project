package com.aymanshehri.whenimthere;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aymanshehri.whenimthere.models.Friend;
import com.aymanshehri.whenimthere.services.MyFirebaseGetter;
import com.aymanshehri.whenimthere.ui.main.MainListFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.rv_friends_list)
    RecyclerView recyclerView;
    private FriendsListAdapter adapter;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    static FriendsListFragment newInstance() {
        return new FriendsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        ButterKnife.bind(this, view);

        Query query = MyFirebaseGetter.getFriendsList();

        FirestoreRecyclerOptions<Friend> options = new FirestoreRecyclerOptions.Builder<Friend>().setQuery(query, Friend.class).build();

        adapter = new FriendsListAdapter(options, new OnFriendClick() {
            @Override
            public void onClick(String value) {
                onFriendClicked(value);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void onFriendClicked(String email) {
        if (mListener != null) {
            mListener.onFragmentInteraction(email);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener)getParentFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String email);
    }
}
