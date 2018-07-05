package ateam.foodr;

import java.util.ArrayList;

public class Food {


    // TODO: Make price an integer

    private int rate;
    private String name,desc, Imageurl,uid;
    private String comments;
    private double numOfRating, totalOfRating;


    public Food() {

    }

    public Food(String uid, String name, String desc, int rate, double numOfRating, double totalOfRating , String imageurl, String comments) {
        if(name.trim().equals("")){
            name = "NO Name";
        }
        this.rate = rate;
        this.name = name;
        this.desc = desc;
        Imageurl = imageurl;
        this.uid = uid;
        this.comments = comments;
        this.numOfRating = numOfRating;
        this.totalOfRating = totalOfRating;
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

    public double getNumOfRating() {
        return numOfRating;
    }

    public void setNumOfRating(double numOfRating) {
        this.numOfRating = numOfRating;
    }

    public double getTotalOfRating() {
        return totalOfRating;
    }

    public void setTotalOfRating(double totalOfRating) {
        this.totalOfRating = totalOfRating;
    }
}
