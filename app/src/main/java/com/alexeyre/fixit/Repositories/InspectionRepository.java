package com.alexeyre.fixit.Repositories;

import com.alexeyre.fixit.Models.CallBack;
import com.google.firebase.database.DatabaseReference;

public interface InspectionRepository {

    void createInspection(DatabaseReference databaseReference, Object inspectionModel, CallBack callBack);

}
