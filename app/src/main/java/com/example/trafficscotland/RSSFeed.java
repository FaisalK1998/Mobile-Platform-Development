//Faisal Khan
//S1828698

package com.example.trafficscotland;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RSSFeed extends AppCompatActivity {

    //RSS feed from Traffic Scotland
    private String urlSource1="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String urlSource2="https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String urlSource3="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    //Declare variables
    private TextView urlInput;
    private String result = "";
    private ListView listView;
    ProgressBar progressBar;
    Toolbar toolbar;
    DatePickerDialog.OnDateSetListener dateSetListener;

    //Current Incidents stored in a list
    List<CurrentIncidents> currentIncidentsList;
    ListViewAdapter listViewAdapter;

    ////////////////////////////////////////////

    protected void onCreate(Bundle savedInstanceState) {

        //Sets the layout to show the rss feed
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_feed);

        //Declare all views and objects
        currentIncidentsList = new ArrayList<>();
        listView = findViewById(R.id.list);
        progressBar = findViewById((R.id.progressBar));
        progressBar.setVisibility(View.INVISIBLE);

        //Decides which activity is being called
        if (MainActivity.getIsCurrentIncidents() == true)
        {
            getSupportActionBar().setTitle("Current Incidents");
            startProgress(urlSource1);
        }
        else if (MainActivity.getIsRoadworks() == true)
        {
            getSupportActionBar().setTitle("Current Roadworks");
            startProgress(urlSource2);
        }
        else if (MainActivity.getIsPlannedRoadworks() == true)
        {
            getSupportActionBar().setTitle("Planned Roadworks");
            startProgress(urlSource3);
        }


        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker picker, int year, int month, int dayOfMonth) {

                month = month + 1;

                String dateString = Integer.toString(dayOfMonth) + month + year;

                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMyyyy");
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMMM yyyy");

                try {
                    Date searchDate = dateFormat.parse(dateString);
                    String theSearchDate = dateFormat1.format(searchDate);

                    List<CurrentIncidents> newList = new ArrayList<>();
                    for (CurrentIncidents item : currentIncidentsList)
                    {
                        if (item.startDate.equals(theSearchDate))
                        {
                            newList.add(item);
                        }
                    }

                    listViewAdapter = new ListViewAdapter(getApplicationContext(), R.layout.list_view, newList);
                    listView.setAdapter(listViewAdapter);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                CurrentIncidents item = (CurrentIncidents)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), RSSDetailedInformation.class);
                intent.putExtra("CurrentIncidents", item);

                startActivity(intent);
            }
        });

    }//End of onCreate

    ////////////////////////////////////////////
    /*
    This method handles the search feature of the application
     */
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.viewer_menu, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.searchIcon));
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    } //End of onCreateOptionsMenu

    ////////////////////////////////////////////
    /*
    This method handles the calendar feature of the application
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.calendarIcon)
        {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, R.style.Theme_AppCompat_Light_Dialog_MinWidth,
                    dateSetListener, year, month, day);

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            datePickerDialog.show();
        }
        else if (id == R.id.searchIcon) {}
        return true;
    }//End of onOptionsItemsSelected

    /////////////////////////////////////////////

    public void startProgress(String theUrl)
    {
        //Run network access on a separate thread
        Task newTask = new Task(theUrl);
        newTask.execute();
    }//End of startProgress

    ////////////////////////////////////////////

    /*
    This class collects the required data from Traffic Scotland
    and parses the information into a readable format
     */
    class Task extends AsyncTask <Void, Void, List<CurrentIncidents>>
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<CurrentIncidents> doInBackground(Void... aVoid)
        {
            URL aurl;
            URLConnection urlConnection;
            BufferedReader bufferedReader = null;
            String inputLine = "";
            CurrentIncidents currentIncidentsItem = null;

            Log.e("MyTag", "in run");

            try
            {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                urlConnection = aurl.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                while ((inputLine = bufferedReader.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag", inputLine);
                }
                bufferedReader.close();
            }
            catch (IOException ex)
            {
                Log.e("MyTag", "IOException");
            }

            //XMLPullParser is used to collect the data from the RSS feed
            if (result != null)
            {
                try
                {
                    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                    parserFactory.setNamespaceAware(true);

                    XmlPullParser pullParser = parserFactory.newPullParser();
                    pullParser.setInput(new StringReader(result));

                    int event = pullParser.getEventType();

                    while (event != XmlPullParser.END_DOCUMENT)
                    {
                        switch (event)
                        {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                //Setting the Current Incidents item object
                                if (pullParser.getName().equalsIgnoreCase("item"))
                                {
                                    currentIncidentsItem = new CurrentIncidents("", "", "", "", "", "");
                                }
                                else if (currentIncidentsItem != null)
                                {
                                    if (pullParser.getName().equalsIgnoreCase("title"))
                                    {
                                        currentIncidentsItem.setTitle(pullParser.nextText().trim());
                                    }
                                    else if (pullParser.getName().equalsIgnoreCase("description"))
                                    {
                                        currentIncidentsItem.setDescription(pullParser.nextText().trim());

                                        /*This statement collects the information required based on the data available from the
                                            RSS feed for planned roadworks and parses it accordingly.
                                         */
                                        if (MainActivity.getIsPlannedRoadworks() == true)
                                        {
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");

                                            String[] splitWorks = currentIncidentsItem.getDescription().split(":");
                                            String replaceWorks = splitWorks[6].replaceAll("Traffic Management", "").trim();
                                            currentIncidentsItem.setWorks(replaceWorks);

                                            String replaceTrafficInfo = splitWorks[7].replaceAll(
                                                    "Diversion Information", " ");
                                            currentIncidentsItem.setTrafficManagement(replaceTrafficInfo);

                                            String [] splitStartDate = splitWorks[2].split(", ");
                                            String myStartDate = splitStartDate[1].replaceAll("- 00", "").trim();
                                            Date theStartDate = simpleDateFormat.parse(myStartDate);
                                            currentIncidentsItem.setStartDate = theStartDate;

                                            String [] splitEndDate = splitWorks[4].split(", ");
                                            String myEndDate = splitEndDate[1].replaceAll("- 00", "").trim();
                                            Date theEndDate = simpleDateFormat.parse(myEndDate);
                                            currentIncidentsItem.setEndDate = theEndDate;

                                            currentIncidentsItem.startDate = simpleDateFormat.format(theStartDate);
                                            currentIncidentsItem.endDate = simpleDateFormat.format(theEndDate);

                                            if (currentIncidentsItem.getDescription().contains("Diversion Information"))
                                            {
                                                String splitDiversionInfo = currentIncidentsItem.getDescription().substring(currentIncidentsItem.getDescription().indexOf("Diversion Information:"));
                                                splitDiversionInfo = splitDiversionInfo.replaceAll("Diversion Information:", "");
                                                currentIncidentsItem.setDiversionInfo(splitDiversionInfo);
                                            }
                                        }
                                        /*This statement collects the information required based on the data available from the
                                            RSS feed for current roadworks and parses it accordingly.
                                         */
                                        else if (MainActivity.getIsRoadworks() == true)
                                        {
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");

                                            String [] splitWorks = currentIncidentsItem.getDescription().split(":");
                                            String replaceDelayInfo = splitWorks[0].replaceAll("Delay Information", "").trim();
                                            currentIncidentsItem.setDelayInfo(replaceDelayInfo);

                                            String [] splitStartDate = splitWorks[2].split(", ");
                                            String myStartDate = splitStartDate[1].replaceAll("- 00", "").trim();
                                            Date theStartDate = simpleDateFormat.parse(myStartDate);
                                            currentIncidentsItem.setStartDate = theStartDate;

                                            String [] splitEndDate = splitWorks[4].split(", ");
                                            String myEndDate = splitEndDate[1].replaceAll("- 00", "").trim();
                                            Date theEndDate = simpleDateFormat.parse(myEndDate);
                                            currentIncidentsItem.setEndDate = theEndDate;

                                            currentIncidentsItem.startDate = simpleDateFormat.format(theStartDate);
                                            currentIncidentsItem.endDate = simpleDateFormat.format(theEndDate);

                                            if (currentIncidentsItem.getDescription().contains("Diversion Information"))
                                            {
                                                String splitDiversionInfo = currentIncidentsItem.getDescription().substring(currentIncidentsItem.getDescription().indexOf("Diversion Information:"));
                                                splitDiversionInfo = splitDiversionInfo.replaceAll("Diversion Information:", "");
                                                currentIncidentsItem.setDiversionInfo(splitDiversionInfo);
                                            }
                                            else if (currentIncidentsItem.getDescription().contains("Delay Information"))
                                            {
                                                String splitDelayInfo = currentIncidentsItem.getDescription().substring(currentIncidentsItem.getDescription().indexOf("Delay Information:"));
                                                splitDelayInfo = splitDelayInfo.replaceAll("Delay Information","");
                                                currentIncidentsItem.setDelayInfo(splitDelayInfo);
                                            }
                                        }
                                        else
                                        {

                                        }
                                    }
                                    else if (pullParser.getName().equalsIgnoreCase("link"))
                                    {
                                        currentIncidentsItem.setLink(pullParser.nextText().trim());
                                    }
                                    else if (pullParser.getName().equalsIgnoreCase("pubDate"))
                                    {
                                        currentIncidentsItem.setDate(pullParser.nextText().trim());
                                    }
                                    else if (pullParser.getName().equalsIgnoreCase("point"))
                                    {
                                        String latLonString = pullParser.nextText().trim();
                                        String lonString  = latLonString.substring(latLonString.indexOf("-"));
                                        String latString = latLonString.replaceAll(lonString, "");

                                        currentIncidentsItem.setLat(latString);
                                        currentIncidentsItem.setLon(lonString);
                                    }
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if (pullParser.getName().equalsIgnoreCase("item") && currentIncidentsItem != null)
                                {
                                    currentIncidentsList.add(currentIncidentsItem);
                                }
                                break;
                        }
                        event = pullParser.next();
                    }
                }
                catch (XmlPullParserException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
            return currentIncidentsList;
        }

        @Override
        protected void onPostExecute(List<CurrentIncidents> currentIncidents) {
            listViewAdapter = new ListViewAdapter(getApplicationContext(), R.layout.list_view, currentIncidents);
            listView.setAdapter(listViewAdapter);
        }
    }//End of class Task

}
