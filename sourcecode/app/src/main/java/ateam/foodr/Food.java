package ateam.foodr;

public class Food {

    private int image;
    // TODO: Make price an integer

    private int rate;
    private String name, price, desc, Imageurl;


    /** This constructor is necessary so Firebase will deserialize this class */
    public Food() {}


    public Food(String Imageurl,int image, String name, String price, String desc) {
        this.Imageurl = Imageurl;
        this.image = image;
        this.name = name;
        this.rate =rate;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setPrice(String price) {
        this.rate = rate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }
}
