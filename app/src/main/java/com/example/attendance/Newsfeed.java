package com.example.attendance;

public class Newsfeed {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getdate_of_joining() {
        return date_of_joining;
    }

    public void setdate_of_joining(String eventname) {
        this.eventname = eventname;
    }

    public String geteventname() {
        return eventname;
    }

    public void seteventname(String date_of_joining) {
        this.date_of_joining = date_of_joining;
    }

    private String name;
    private String post_image;
    private String date_of_birth;
    private String date_of_joining;
    private String eventname;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;


    public Newsfeed(String name, String post_image, String date_of_birth, String date_of_joining, String eventname, String phone){
        this.name = name;
        this.post_image = post_image;
        this.date_of_birth = date_of_birth;
        this.date_of_joining = date_of_joining;
        this.eventname = eventname;
        this.phone = phone;
    }
}
