package com.aymanshehri.whenimthere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewItemActivity extends AppCompatActivity {
    EditText edTitle;
    EditText edDetails;

    String id;
    String title;
    String details;

    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        setTitle("Add Item");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        edTitle = findViewById(R.id.et_title);
        edDetails = findViewById(R.id.et_details);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        title = intent.getStringExtra("TITLE");
        details = intent.getStringExtra("DETAILS");
        if (id != null && title != null && details != null) {
            isEditMode = true;
            setTitle("Edit Item");
            edTitle.setText(title);
            edDetails.setText(details.trim());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                saveItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveItem() {
        String newTitle = edTitle.getText().toString();
        String newDetails = edDetails.getText().toString();

        if (newTitle.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_LONG).show();//todo replace with snakebar
            return;
        }

        if (newDetails.trim().isEmpty())
            newDetails = " ";
        Item newItem = new Item(newTitle, newDetails);
        CollectionReference collectionReference;
        collectionReference = FirebaseFirestore.getInstance().collection("items");
        if (isEditMode) {
            DocumentReference documentReference = collectionReference.document(id);
            documentReference.set(newItem);
        } else
            collectionReference.add(newItem);
        Toast.makeText(this, "Item Saved", Toast.LENGTH_LONG).show();//todo replace with snakebar
        finish();
    }


}
