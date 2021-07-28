package com.example.maps.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.maps.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maps.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMinZoomPreference(15);      //set the min zoom that user can do
        mMap.setMaxZoomPreference(19);      //set the max zoom that user can do


        // ------------- Add a marker in Sydney and move the camera
        //Get Latitud and Longitud of the target what i want to see in the map
        LatLng target = new LatLng(40.74733687259361, -73.98605592751366);
        // Add a market at the position that i want to see
        mMap.addMarker(new MarkerOptions().position(target).title("Marker in my House").draggable(true));
        //Create a camera position to see the target
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(target)    //set the target
                .zoom(18)           //set the zoom that i want 1--word, 5--continent, 10--city, 15--streets, 20--builders,  limit --21
                .bearing(0)        //orientation the camera to west, limit --365
                .tilt(30)           //effect 3D, limit 90, works only in some cities
                .build();
        //set the animation the camera to my target
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //set to what position the camera should move
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(target));
    }
}