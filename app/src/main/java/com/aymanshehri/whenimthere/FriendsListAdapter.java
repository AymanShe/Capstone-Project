package com.aymanshehri.whenimthere;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aymanshehri.whenimthere.models.Friend;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FriendsListAdapter extends FirestoreRecyclerAdapter<Friend, FriendsListAdapter.FriendHolder> {

    public FriendsListAdapter(@NonNull FirestoreRecyclerOptions<Friend> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendHolder friendHolder, int i, @NonNull Friend friend) {
        friendHolder.email.setText(friend.getEmail());
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FriendHolder(view);
    }

    class FriendHolder extends RecyclerView.ViewHolder{
        TextView email;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.tv_email);
        }
    }
}
