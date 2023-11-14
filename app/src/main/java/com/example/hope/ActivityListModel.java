package com.example.hope;

public class ActivityListModel
{
    private String Activity;
    private String Location;
    private String Date;
    private String Time;
    private boolean Approved;

    public ActivityListModel()
    {

    }


    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public boolean isApproved() {
        return Approved;
    }

    public void setApproved(boolean approved) {
        Approved = approved;
    }

    public ActivityListModel(String activity, String location, String date, String time, boolean approved) {
        Activity = activity;
        Location = location;
        Date = date;
        Time = time;
        Approved = approved;
    }
}
