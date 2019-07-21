package com.aymanshehri.whenimthere;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class ItemAdapter extends FirestoreRecyclerAdapter<Item, ItemAdapter.ItemHolder> {
    private Context context;

    ItemAdapter(@NonNull FirestoreRecyclerOptions<Item> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemHolder itemHolder, int i, @NonNull Item item) {
        itemHolder.title.setText(item.getTitle());
        itemHolder.details.setText(item.getDetails());
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_item, parent, false);
        return new ItemHolder(view);
    }

    void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    class ItemHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView details;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            details = itemView.findViewById(R.id.tv_details);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);

                        Item clickedItem = documentSnapshot.toObject(Item.class);
                        String id = documentSnapshot.getId();

                        DocumentReference documentReference = documentSnapshot.getReference();//todo remove if not needed
                        String path = documentReference.getPath();//todo remove if not needed

                        Intent intent = new Intent(context, NewItemActivity.class);

                        assert clickedItem != null;
                        intent.putExtra("ID", id);
                        intent.putExtra("TITLE", clickedItem.getTitle());
                        intent.putExtra("DETAILS", clickedItem.getDetails());
                        context.startActivity(intent);

                        //you have to pass the context
                    }
                }
            });
        }
    }
}
