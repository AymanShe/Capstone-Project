package com.aymanshehri.whenimthere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.aymanshehri.whenimthere.services.MyFirebaseGetter;
import com.aymanshehri.whenimthere.services.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewContributorActivity extends AppCompatActivity {

    @BindView(R.id.et_contributor_email)
    EditText contributorEmail;
    @BindView(R.id.addContributorButton)
    Button addContributorButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contributor);
        ButterKnife.bind(this);

        addContributorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContributor(v);
            }
        });
    }

    public void addContributor(View v) {
        //hide the keyboard to show the snackbar
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String email = contributorEmail.getText().toString();
        //check if valid input
        if(!Validator.validEmail(contributorEmail)){
            return;
        }
        //check if email doesn't belong to user
        if(email.equals(MyFirebaseGetter.getUserEmail())){
            Snackbar.make(getCurrentFocus(),"You can add yourself",Snackbar.LENGTH_SHORT).show();
            return;
        }
        //add contributor
        MyFirebaseGetter.addContributor(v, contributorEmail);
    }
}
