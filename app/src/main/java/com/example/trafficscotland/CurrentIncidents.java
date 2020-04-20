//Faisal Khan
//S1828698

package com.example.trafficscotland;

import java.io.Serializable;
import java.util.Date;

public class CurrentIncidents implements Serializable {

    //Declare variables
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String lat;
    private String lon;

    private String works;
    private String trafficManagement;
    private String diversionInfo;
    private String delayInformation;

    public Date setStartDate;
    public Date setEndDate;
    public String startDate;
    public String endDate;

    //CurrentIncidents constructor
    public CurrentIncidents(String title, String description, String link, String pubDate, String lat, String lon)
    {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.lat = lat;
        this.lon = lon;
    }

    //Getters and Setters
    //Title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //Description
    public String getDescription() {
        return "Incident Information: " + "\n" + description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //Link
    public String getLink() {
        return "For more information: " + link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    //pubDate
    public String getDate() {
        return "Date Published: " + pubDate;
    }

    public void setDate(String pubDate) {
        this.pubDate = pubDate;
    }

    //Latitude
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    //Longitude
    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    //Latitude and Longitude
    public String getLatLon() {
        return "Latitude is: " + lat + "" + "" + "" + "" + "Longitude is: " + lon;
    }

    //Works
    public String getWorks() {
        return works;
    }

    public void setWorks(String works) {
        this.works = works;
    }

    //Traffic Management
    public String getTrafficManagement() {
        return trafficManagement;
    }

    public void setTrafficManagement(String trafficManagement) {
        this.trafficManagement = trafficManagement;
    }

    //Diversion Information
    public String getDiversionInfo() {
        return diversionInfo;
    }

    public void setDiversionInfo(String diversionInfo) {
        this.diversionInfo = diversionInfo;
    }

    //Delay Information
    public String getDelayInfo()
    {
        return delayInformation;
    }

    public void setDelayInfo(String delayInformation) {
        this.delayInformation = delayInformation;
    }

    //Start Date
    public Date getStartDate() {
        return setStartDate;
    }

    public void setSetStartDate(Date setDate) {
        this.setStartDate = setDate;
    }

    //End Date
    public Date getEndDate() {
        return setEndDate;
    }

    public void setEndDate(Date setDate) {
        this.setEndDate = setDate;
    }

    //Calculate Days of works
    public int calculateDays(Date date1, Date date2)
    {
        if (date1 != null && date2 != null)
        {
            int days = ((int) ((date2.getTime()/(24*60*60*1000))- (int) (date1.getTime()/(24*60*60*1000))));
            return days;
        }
        else
        {
            return 0;
        }
    }

    //Calculate duration of works in days
    public int getDurationInDays()
    {
        int days = calculateDays(setStartDate, setEndDate);
        return days;
    }

    //Retrieve Works and Traffic description
    public String getWorksAndTrafficDescription()
    {
        return "Current Works: " + works + "\n" + "\n" + "Traffic Management: " + trafficManagement + "\n";
    }

    //Retrieve roadworks description
    public String getRoadworksDescription()
    {
        return "Current Works: " + works + "\n" + "\n" + "Traffic Management: " + trafficManagement + "\n" + "\n" + "Diversion Information: " + diversionInfo;
    }

    //Retrieve delay information
    public String getDelayInformation()
    {
        return "Delay Information " + delayInformation;
    }

    //Retrieve start and end date for works
    public String getStartEndDate()
    {
        return ("Start date: " + startDate + " " + " " + " " + "End Date: " + endDate
                + "\n" + "Estimated Time of Works: " + getDurationInDays() + " days");
    }

    //Returns the current incidents description
    public String returnCurrentIncidentDescription()
    {
        return title;
    }
}
