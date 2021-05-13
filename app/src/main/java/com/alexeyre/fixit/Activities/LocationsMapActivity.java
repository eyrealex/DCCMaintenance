package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LocationsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    private float MAP_ZOOM = 12;
    private ArrayList<TrafficLightModel> allTrafficLightsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_map);
        initComponents();
    }

    /**
     * Setup all functionality
     */
    private void initComponents() {
        //Setup Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    /**
     * This will render all locations on the map each time it is called
     */
    private void createMapMarkers() {

        try {
            if (mMap != null) {
                mMap.clear(); // Clear any old markers
                for (TrafficLightModel trafficLightModel : allTrafficLightsList) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(trafficLightModel.getlatitude()),
                            Double.parseDouble(trafficLightModel.getlongitude()))).title(trafficLightModel.getname()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getData() {
        //Get Coordinates from Firebase
        //This will trigger the onDataChange once there is a live change in the database under the coordinates node
        FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allTrafficLightsList.clear();

                for (DataSnapshot trafficLightDataSnapshot : snapshot.getChildren()) {
                    try{
                        TrafficLightModel newTrafficLightObject = trafficLightDataSnapshot.getValue(TrafficLightModel.class);
                        allTrafficLightsList.add(newTrafficLightObject);
                    }catch(Exception e){

                    }

                }

                createMapMarkers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.3498, -6.2603), MAP_ZOOM));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showMarkerDetails(marker);
                return false;
            }
        });
        getData();
    }

    private void showMarkerDetails(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
    }

    public void list_view_btn(View view) {
        startActivity(new Intent(LocationsMapActivity.this, LocationsListActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}