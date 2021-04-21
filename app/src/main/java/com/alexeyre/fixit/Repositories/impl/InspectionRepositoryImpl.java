package com.alexeyre.fixit.Repositories.impl;

import android.app.Activity;

import com.alexeyre.fixit.Models.CallBack;
import com.alexeyre.fixit.R;
import com.alexeyre.fixit.Repositories.FirebaseRepository;
import com.alexeyre.fixit.Repositories.InspectionRepository;
import com.google.firebase.database.DatabaseReference;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.alexeyre.fixit.Constants.Constants.SUCCESS;

public class InspectionRepositoryImpl extends FirebaseRepository implements InspectionRepository {

    private Activity activity;
    private SweetAlertDialog progressDialog;

    public InspectionRepositoryImpl(Activity activity) {
        this.activity = activity;
        progressDialog = new SweetAlertDialog(activity);
        progressDialog.getProgressHelper().setBarColor(activity.getResources().getColor(R.color.colorAccent));
        progressDialog.setTitleText("Loading");
        progressDialog.setContentText("Please wait...");
    }


    @Override
    public void createInspection(DatabaseReference databaseReference, Object inspectionModel, CallBack callBack) {
        progressDialog.show();

        firebaseCreate(databaseReference, inspectionModel, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                //Create receipt


                progressDialog.dismissWithAnimation();
                callBack.onSuccess(SUCCESS);
            }

            @Override
            public void onError(Object object) {
                progressDialog.dismissWithAnimation();
                callBack.onError(object);
            }
        });
    }
}
