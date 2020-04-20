//Faisal Khan
//S1828698

package com.example.trafficscotland;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Declare variables and components
    public Button CurrentIncidentsBtn;
    public Button PlannedRoadworksBtn;
    public Button RoadworksBtn;
    public static Boolean isCurrentIncidents;
    public static Boolean isPlannedRoadworks;
    public static Boolean isRoadworks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declare all views
        CurrentIncidentsBtn = findViewById(R.id.CurrentIncidentsBtn);
        CurrentIncidentsBtn.setOnClickListener(this);
        PlannedRoadworksBtn = findViewById(R.id.PlannedRoadworksBtn);
        PlannedRoadworksBtn.setOnClickListener(this);
        RoadworksBtn = findViewById(R.id.RoadworksBtn);
        RoadworksBtn.setOnClickListener(this);
    }

    /*
    OnClick method is used to set what each button should do when selected
     */
    public void onClick(View theView)
    {
        Intent theIntent;
        if (theView == CurrentIncidentsBtn)
        {
            isCurrentIncidents = true;
            isPlannedRoadworks = false;
            isRoadworks = false;
            theIntent = new Intent(getApplicationContext(), RSSFeed.class);
            startActivity(theIntent);
        }
        else if (theView == PlannedRoadworksBtn)
        {
            isCurrentIncidents = false;
            isPlannedRoadworks = true;
            isRoadworks = false;
            theIntent = new Intent(getApplicationContext(), RSSFeed.class);
            startActivity(theIntent);
        }
        else if (theView == RoadworksBtn)
        {
            isCurrentIncidents = false;
            isPlannedRoadworks = false;
            isRoadworks = true;
            theIntent = new Intent(getApplicationContext(), RSSFeed.class);
            startActivity(theIntent);
        }
    }

    //Returns Current Incidents
    public static Boolean getIsCurrentIncidents()
    {
        return isCurrentIncidents;
    }

    //Returns Planned Roadworks
    public static Boolean getIsPlannedRoadworks()
    {
        return isPlannedRoadworks;
    }

    //Returns Current Roadworks
    public static Boolean getIsRoadworks()
    {
        return isRoadworks;
    }
}
