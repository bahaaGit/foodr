package ateam.foodr;

public class Food {
    private int image,rate;
    private String name, price, desc;



    public Food(int image, String name, String desc,int rate) {
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
}
