package com.aymanshehri.whenimthere;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListFragment extends Fragment {


    private ItemAdapter adapter;

    private FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
    private CollectionReference itemRef = dbRef.collection("items");

    private boolean isGotList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_items, container, false);
        FloatingActionButton addItemButton = view.findViewById(R.id.fab_add_item);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewItemActivity.class));
            }
        });

        isGotList = false;
        Bundle passedBundle = getArguments();
        String isGotListKey = "isGotListKey";
        if (passedBundle != null)
            isGotList = passedBundle.getBoolean(isGotListKey);
        boolean test = isGotList;

        Query query = itemRef.whereEqualTo("got", isGotList);

        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        adapter = new ItemAdapter(options, getContext());

        RecyclerView recyclerView = view.findViewById(R.id.rv_item_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            
            int iconResourceId;
            Drawable icon;
            int intrinsicWidth;
            int intrinsicHeight;
            int iconTop;
            int iconMargin;
            int iconLeft;
            int iconRight;
            int iconBottom;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.deleteItem(viewHolder.getAdapterPosition());
                } else {
                    adapter.toggleStatus(viewHolder.getAdapterPosition());
                }

            }

            //The swipe to delete color effect is taken from https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getBottom() - itemView.getTop();

                ColorDrawable background = new ColorDrawable();

                if (dX < 0) {
                    // Draw the red background on the canvas
                    String swipeLeftColor = "#f44336";
                    background.setColor(Color.parseColor(swipeLeftColor));
                    background.setBounds(itemView.getRight() + Math.round(dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    background.draw(c);

                    // Declare the delete icon
                    iconResourceId = R.drawable.ic_delete;
                    icon = ContextCompat.getDrawable(getContext(), iconResourceId);
                    intrinsicWidth = icon.getIntrinsicWidth();
                    intrinsicHeight = icon.getIntrinsicHeight();

                    // Calculate position of delete icon
                    iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                    iconMargin = (itemHeight - intrinsicHeight) / 2;
                    iconLeft = itemView.getRight() - iconMargin - intrinsicWidth;
                    iconRight = itemView.getRight() - iconMargin;
                    iconBottom = iconTop + intrinsicHeight;

                    // Draw the delete icon on the canvas
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    icon.draw(c);
                } else {
                    // Draw the red background on the canvas
                    String swipeRightColor;
                    if (isGotList)
                        swipeRightColor = "#F49136";
                    else
                        swipeRightColor = "#29BB43";
                    background.setColor(Color.parseColor(swipeRightColor));
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + Math.round(dX), itemView.getBottom());
                    background.draw(c);

                    // Declare the check icon
                    if (isGotList)
                        iconResourceId = R.drawable.ic_return;
                    else
                        iconResourceId = R.drawable.ic_check;
                    icon = ContextCompat.getDrawable(getContext(), iconResourceId);
                    intrinsicWidth = icon.getIntrinsicWidth();
                    intrinsicHeight = icon.getIntrinsicHeight();

                    // Calculate position of check icon
                    iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                    iconMargin = (itemHeight - intrinsicHeight) / 2;
                    iconLeft = itemView.getLeft() + iconMargin;
                    iconRight = itemView.getLeft() + iconMargin + intrinsicWidth;
                    iconBottom = iconTop + intrinsicHeight;

                    // Draw the delete icon on the canvas
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    icon.draw(c);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

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
