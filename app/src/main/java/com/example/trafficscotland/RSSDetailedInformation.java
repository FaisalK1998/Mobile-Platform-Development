//Faisal Khan
//S1828698

package com.example.trafficscotland;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class RSSDetailedInformation extends RSSFeed implements View.OnClickListener{

    //Declare variables, objects and view components
    CurrentIncidents theItem;
    TextView descriptionText;
    TextView linkText;
    TextView publishDateText;
    TextView startEndDateText;
    TextView latLonText;
    public Button mapButton;
    public static Boolean isMapButton;

    protected void onCreate(Bundle savedInstanceState) {

        //Sets view to detailed list view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_list_view);

        theItem = (CurrentIncidents) getIntent().getSerializableExtra("CurrentIncidents");
        getSupportActionBar().setTitle(theItem.getTitle());

        //Declare all views
        descriptionText = findViewById(R.id.descriptionText);
        linkText = findViewById(R.id.linkText);
        publishDateText = findViewById(R.id.publishDateText);
        latLonText = findViewById(R.id.latLonText);
        startEndDateText = findViewById(R.id.startEndDateText);
        //mapButton = findViewById(R.id.mapButton);
        //mapButton.setOnClickListener(this);

        /*
        If statement checks which statement is fulfilled and executes the relative code
         */
        if (theItem.getWorks() != null && theItem.getTrafficManagement() != null) {
            descriptionText.setText(theItem.getWorksAndTrafficDescription());

            if (theItem.getDiversionInfo() != null) {
                descriptionText.setText((theItem.getRoadworksDescription()));
            }
            startEndDateText.setText(theItem.getStartEndDate());

        } else if (theItem.getWorks() == null && theItem.getTrafficManagement() == null) {

            if (theItem.getDelayInfo() != null) {
                descriptionText.setText(theItem.getDelayInformation());
                startEndDateText.setText(theItem.getStartEndDate());
            } else {
                descriptionText.setText((theItem.getDescription()));
            }

        } else {
            descriptionText.setText(theItem.getDescription());
        }
        linkText.setText(theItem.getLink());
        publishDateText.setText(theItem.getDate());
        latLonText.setText(theItem.getLatLon());
    }


    public void onClick(View theView)
    {
        Intent theIntent;
        if (theView == mapButton) {
            isMapButton = true;
            theIntent = new Intent(getBaseContext(), MapsMarkerActivity.class);
            startActivity(theIntent);
            //setContentView(R.layout.maps_view);
        }
    }
}
