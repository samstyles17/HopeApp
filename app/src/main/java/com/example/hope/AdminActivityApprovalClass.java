package com.example.hope;

public class AdminActivityApprovalClass
{
    private String Name,Activity,Location,Date,Time,Email;
    private boolean Approval;
    private String documentId;



    public AdminActivityApprovalClass()
    {

    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public boolean isApproval() {
        return Approval;
    }

    public void setApproval(boolean approval) {
        Approval = approval;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public AdminActivityApprovalClass(String name, String activity, String location, String date, String time, String email, boolean approval, String documentId) {
        Name = name;
        Activity = activity;
        Location = location;
        Date = date;
        Time = time;
        Email = email;
        Approval = approval;
        this.documentId = documentId;
    }
}
