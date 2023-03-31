package com.example.samplemvc.model.bean;

public class ToDo {

    private long id;
    private String title;
    private String details;
    private String date;
    private String time;
    private int notificationStatus;


    public ToDo(){
        super();
    }

    public ToDo(long id, String title){
        this.id=id;
        this.title=title;
    }
    public ToDo(long id, String title, String details, String date, String time, int notificationStatus) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.date = date;
        this.time = time;
        this.notificationStatus = notificationStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(int notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    @Override
    public String toString() {
        return "("+ id+", "+title+", "+details+")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ToDo){
            ToDo temp = (ToDo) obj;
            if(temp.id==this.id){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
