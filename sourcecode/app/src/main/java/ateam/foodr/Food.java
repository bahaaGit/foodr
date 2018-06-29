package ateam.foodr;

public class Food {
    private String image;
    private String name, price, desc;   // TODO: Make price an integer

    /** This constructor is necessary so Firebase will deserialize this class */
    public Food() {}

    public Food(String image, String name, String price, String desc) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
