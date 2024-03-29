package com.aymanshehri.whenimthere;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aymanshehri.whenimthere.models.Item;
import com.aymanshehri.whenimthere.services.MyFirebaseGetter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    private static final String IS_GOT_LIST_EXTRA_KEY = "isGotListExtraKey";
    private static final String USER_EMAIL_EXTRA_KEY = "userEmailExtraKey";
    @BindView(R.id.rv_item_list)
    RecyclerView recyclerView;
    @BindView(R.id.fab_add_item)
    FloatingActionButton addItemButton;
    private ItemAdapter adapter;

    private boolean isGotList;
    private String userEmail;

    public static ListFragment newInstance(boolean param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_GOT_LIST_EXTRA_KEY, param1);
        args.putString(USER_EMAIL_EXTRA_KEY, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isGotList = getArguments().getBoolean(IS_GOT_LIST_EXTRA_KEY);
            userEmail = getArguments().getString(USER_EMAIL_EXTRA_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_items, container, false);
        ButterKnife.bind(this, view);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(USER_EMAIL_EXTRA_KEY, userEmail);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });//todo change to an alert dialog

        isGotList = false;
        Bundle passedBundle = getArguments();
        if (passedBundle != null) {
            isGotList = passedBundle.getBoolean(IS_GOT_LIST_EXTRA_KEY);

            Query query = MyFirebaseGetter.listQuery(userEmail, isGotList);

            FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                    .setQuery(query, Item.class)
                    .build();

            adapter = new ItemAdapter(options, getContext());

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }


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
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    adapter.deleteItem(viewHolder.getAdapterPosition());
                                    Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    adapter.restoreItem(viewHolder.getAdapterPosition());
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).setCancelable(false).show();
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

                if (dX < 0) {//swipe left to delete
                    // Draw the red background on the canvas
                    String swipeLeftColor = "#f44336";
                    background.setColor(Color.parseColor(swipeLeftColor));
                    background.setBounds(itemView.getRight() + Math.round(dX), itemView.getTop(), itemView.getRight() - Math.round(dX), itemView.getBottom());
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
                } else {//swipe right to toggle status
                    // Draw the red background on the canvas
                    String swipeRightColor;
                    if (isGotList)
                        swipeRightColor = "#F49136";
                    else
                        swipeRightColor = "#29BB43";
                    background.setColor(Color.parseColor(swipeRightColor));
                    background.setBounds(itemView.getLeft() - Math.round(dX), itemView.getTop(), itemView.getLeft() + Math.round(dX), itemView.getBottom());
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
