package com.alexeyre.fixit.Repositories;

import com.alexeyre.fixit.Models.CallBack;
import com.google.firebase.database.DatabaseReference;

import static com.alexeyre.fixit.Constants.Constants.SUCCESS;

public abstract class FirebaseRepository {

    protected final void firebaseCreate(DatabaseReference databaseReference, Object object, CallBack callBack) {
        databaseReference.keepSynced(true);

        databaseReference.setValue(object, (databaseError, databaseReference1) -> {
            if (databaseError != null) {
                callBack.onError(databaseError);
            } else {
                callBack.onSuccess(SUCCESS);
            }
        });
    }

    //firebaseRead

    //firebaseUpdate

    //firebaseDelete
}
