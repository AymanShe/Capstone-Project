package com.aymanshehri.whenimthere;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aymanshehri.whenimthere.models.Friend;
import com.aymanshehri.whenimthere.services.MyFirebaseGetter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsListFragment extends Fragment {

    @BindView(R.id.rv_friends_list)
    private RecyclerView recyclerView;
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
        ButterKnife.bind(this,view);

        Query query = MyFirebaseGetter.getFriendsList();

        FirestoreRecyclerOptions<Friend> options = new FirestoreRecyclerOptions.Builder<Friend>().setQuery(query, Friend.class).build();

        adapter = new FriendsListAdapter(options);

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
}
