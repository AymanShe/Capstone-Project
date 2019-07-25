package com.aymanshehri.whenimthere.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyFirebaseGetter {
    //Firestore Methods
    private static FirebaseFirestore getFirestoreInstance(){
        return FirebaseFirestore.getInstance();
    }

    private static CollectionReference getListCollectionReference(){
        return getFirestoreInstance().collection("lists");
    }

    public static Query listQuery(String listOwner, boolean isGotList) {
        return getListCollectionReference().document(listOwner).collection("items").whereEqualTo("got", isGotList);
    }

    public static CollectionReference getItemsCollection(String userEmail) {
        return getListCollectionReference().document(userEmail).collection("items");
    }

    //FirebaseAuth methods
    public static String getUserEmail(){
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
}