package com.example.samplemvc.model.bean;

public class ToDo {

    private long id;
    private String title;
    private String place;
    private String date;
    private String time;


    public ToDo(){
        super();
    }

    public ToDo(long id, String title){
        this.id=id;
        this.title=title;
    }
    public ToDo(long id, String title, String place, String date, String time) {
        this.id = id;
        this.title = title;
        this.place = place;
        this.date = date;
        this.time = time;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    @Override
    public String toString() {
        return "("+ id+", "+title+", "+place+")";
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
