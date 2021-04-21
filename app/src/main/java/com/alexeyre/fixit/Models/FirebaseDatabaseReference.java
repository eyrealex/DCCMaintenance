package com.alexeyre.fixit.Models;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseReference {

    public static FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();

    public static void setDATABASE(FirebaseDatabase firebaseDatabase) {
        FirebaseDatabaseReference.setDATABASE(firebaseDatabase);
    }
}
