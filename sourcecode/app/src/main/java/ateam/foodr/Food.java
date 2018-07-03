package ateam.foodr;

import java.util.ArrayList;

public class Food {


    // TODO: Make price an integer

    private int rate;
    private String name,desc, Imageurl,uid;
    private String comments;


    public Food() {

    }

    public Food(String uid, String name, String desc, int rate, String imageurl, String comments) {
        this.rate = rate;
        this.name = name;
        this.desc = desc;
        Imageurl = imageurl;
        this.uid = uid;
        this.comments = comments;
    }


    public int getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public String getUid() {
        return uid;
    }

    public String getComments() {
        return comments;
    }


    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
