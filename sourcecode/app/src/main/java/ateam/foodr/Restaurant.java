package ateam.foodr;

import android.location.Address;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Restaurant
{
    // Boilerplate accessors


    // Private fields
    private int image;
    private String Imageurl;
    private String name;
    private String description;
    private String address;
    private String businessNumber;
    private String phoneNumber;

    // TODO: Menu array

    /** This constructor is required for Firebase to automatically serialize it */
    public Restaurant() {}

    /** Standard boilerplate constructor. */
    public Restaurant(String Imageurl,int image, String name, String description, String address, String phoneNumber, String businessNumber)
    {
        this.name = name;
        this.Imageurl = Imageurl;
        this.image = image;
        this.description = description;
        this.address = address;
        this.businessNumber = businessNumber;
        this.phoneNumber = phoneNumber;
    }
    public String getName() { return name; }
    public int getImage() { return image; }
    public String getDescription() { return description; }
    public String getAddress() {return address;}
    public String getBusinessNumber() {return businessNumber; }
    public String getPhoneNumber() { return phoneNumber; }

    public String getImageurl() {
        return Imageurl;
    }
    // TODO: addFood method
    // TODO: removeFood method
    // TODO: Some way to access the food items
}
