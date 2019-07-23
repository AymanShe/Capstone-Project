package com.aymanshehri.whenimthere;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyFirebaseGetter {
    //Firestore Methods
    public static FirebaseFirestore getFirestoreInstance(){
        return FirebaseFirestore.getInstance();
    }

    private static CollectionReference getListCollectionReference(){
        return FirebaseFirestore.getInstance().collection("lists");
    }

    static Query listQuery(String listOwner, boolean isGotList) {
        return getListCollectionReference().document(listOwner).collection("items").whereEqualTo("got", isGotList);
    }

    static CollectionReference getItemsCollection(String userEmail) {
        return getListCollectionReference().document(userEmail).collection("items");
    }

    //FirebaseAuth methods
    static String getUserEmail(){
        return getFirebaseAuthInstance().getCurrentUser().getEmail();
    }

    public static FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    static Object getCurrentUser() {
        return getFirebaseAuthInstance().getCurrentUser();
    }
}