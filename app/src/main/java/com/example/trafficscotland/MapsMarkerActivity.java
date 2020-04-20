//Faisal Khan
//S1828698

package com.example.trafficscotland;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.List;

public class MapsMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    CurrentIncidents theItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        theItem = (CurrentIncidents) getIntent().getSerializableExtra("CurrentIncidents");
        getSupportActionBar().setTitle(theItem.getTitle());

        // Retrieve the content view that renders the map.
        setContentView(R.layout.maps_view);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        String latitude = theItem.getLat();
        String longitude = theItem.getLon();
        double Lat = Double.valueOf(latitude);
        double Long = Double.valueOf(longitude);

        // Add a marker in the location of works,
        // and move the map's camera to the same location.
        LatLng location = new LatLng(Lat,Long);
        googleMap.addMarker(new MarkerOptions().position(location)
                        .title("Marker in Location: " + theItem.getTitle()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

    }
}
