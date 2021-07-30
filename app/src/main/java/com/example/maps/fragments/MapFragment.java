package com.example.maps.fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maps.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private View rootView;
    private GoogleMap mMap;
    private MapView mapView;

    private Geocoder geocoder;
    private List<Address> addresses;

    private MarkerOptions markerOptions;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = rootView.findViewById(R.id.mapView);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this::onMapReady);
        }

        checkIfGpsIsEnabled();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkIfGpsIsEnabled();
    }

    private void checkIfGpsIsEnabled(){
        try {
            //get state of gps
            int gpsSignal = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);

            if(gpsSignal == 0){
                //gps is off
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMinZoomPreference(5);      //set the min zoom that user can do
        mMap.setMaxZoomPreference(19);      //set the max zoom that user can do

        //Get Latitud and Longitud of the target what i want to see in the map
        LatLng target = new LatLng(40.74733687259361, -73.98605592751366);

        //Set the zoom of the camera
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        //Instance marker and set features
        markerOptions = new MarkerOptions();
        markerOptions.position(target); //set the target position in map
        markerOptions.title("My market"); //set title to market
        markerOptions.draggable(true); //set market can be dragging
        //markerOptions.snippet("this is a text box in that you can change data");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_on)); //set an icon for the marker

        // Add a market at the position that i want to see
        mMap.addMarker(markerOptions);
        //mMap.addMarker(new MarkerOptions().position(target).title("Marker in NY").draggable(true));

        //Move camera to position of target
        mMap.moveCamera(CameraUpdateFactory.newLatLng(target));

        //Give the zoom to camera
        mMap.animateCamera(zoom);

        //Set the listener for dragging marker
        mMap.setOnMarkerDragListener(this);

        //instance geocoder with default locale
        geocoder = new Geocoder(getContext(), Locale.getDefault());

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;

        try {
            //get a list with address from the target
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();

        //hide snippet
        marker.hideInfoWindow();
        //change info of snippet
        marker.setSnippet("Postal Code: " + postalCode );
        //show snippet with info changed
        marker.showInfoWindow();

        /*Toast.makeText(getContext(), "Country: " + country + "\n"
                + "State: " + state + "\n"
                + "City: " + city + "\n"
                + "Postal Code: " + postalCode + "\n"
                + "address: " + address + "\n",
                Toast.LENGTH_SHORT).show();*/

    }
}