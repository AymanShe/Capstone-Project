package com.aymanshehri.whenimthere.services;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;


public class MyFirebaseGetter {
    //Firestore Methods
    private static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    private static CollectionReference getListCollectionReference() {
        return getFirestoreInstance().collection("lists");
    }

    public static Query listQuery(String listOwner, boolean isGotList) {
        return getListCollectionReference().document(listOwner).collection("items").whereEqualTo("got", isGotList);
    }

    public static CollectionReference getItemsCollection(String userEmail) {
        return getListCollectionReference().document(userEmail).collection("items");
    }

    public static CollectionReference getContributorsCollection() {
        return getListCollectionReference().document(getUserEmail()).collection("contributors");
    }

    //FirebaseAuth methods
    public static String getUserEmail() {
        return getFirebaseAuthInstance().getCurrentUser().getEmail();
    }

    public static FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    public static Object getCurrentUser() {
        return getFirebaseAuthInstance().getCurrentUser();
    }

    public static Query getFriendsList() {
        return getListCollectionReference().document(getUserEmail()).collection("friends");
    }

    public static CollectionReference getFriendsList(String ownerEmail) {
        return getListCollectionReference().document(ownerEmail).collection("friends");
    }

    public static void addContributor(View view, EditText editText) {
        Button button = (Button) view;
        button.setClickable(false);
        Snackbar snackbar = Snackbar.make(view, "Please Wait. Adding in Progress", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();

        String email = editText.getText().toString().trim();

        //check if exists
        getListCollectionReference()
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //check if email already added
                                getContributorsCollection()
                                        .whereEqualTo("email", email)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().size() > 0) {
                                                        snackbar.dismiss();
                                                        button.setClickable(true);
                                                        Snackbar.make(view, email + " is already added.", Snackbar.LENGTH_SHORT).show();
                                                    } else {
                                                        //add contributor
                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("email", email);

                                                        getContributorsCollection().document()
                                                                .set(map)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        //todo use transactions
                                                                        Map<String, Object> map = new HashMap<>();
                                                                        map.put("email", getUserEmail());
                                                                        getFriendsList(email).document().set(map);
                                                                        snackbar.dismiss();
                                                                        button.setClickable(true);
                                                                        Snackbar.make(view, email + " Was Added Successfully.", Snackbar.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        snackbar.dismiss();
                                                                        button.setClickable(true);
                                                                        Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    snackbar.dismiss();
                                                    button.setClickable(true);
                                                    Snackbar.make(view, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                snackbar.dismiss();
                                button.setClickable(true);
                                Snackbar.make(view, email + " doesn't exist.", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            snackbar.dismiss();
                            button.setClickable(true);
                            Snackbar.make(view, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void createUserInDB(String email) {
        Map<String, Object> map = new HashMap<>();
        map.put("active", true);
        getListCollectionReference().document(email).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Get a new write batch
                        WriteBatch batch = getFirestoreInstance().batch();

                        DocumentReference contributorsRef = getListCollectionReference().document(email).collection("contributors").document();
                        batch.set(contributorsRef, new HashMap<>());
                        DocumentReference friendsRef = getListCollectionReference().document(email).collection("friends").document();
                        batch.set(friendsRef, new HashMap<>());
                        DocumentReference itemsRef = getListCollectionReference().document(email).collection("items").document();
                        batch.set(itemsRef, new HashMap<>());

                        // Commit the batch
                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });
                    }
                });
    }
}