package ateam.foodr;

import android.location.Address;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;

public class Restaurant
{
    // Private fields
    private String imageurl;
    private String name;
    private String description;
    private String address;
    private String businessNumber;
    private String phoneNumber;
    private String restID;

    // TODO: Menu array

    /** This constructor is required for Firebase to automatically serialize it */
    public Restaurant() {}

    /** Standard boilerplate constructor. */
    public Restaurant(String Imageurl, String name, String description, String address, String phoneNumber, String businessNumber, String restID)
    {
        this.name = name;
        this.imageurl = Imageurl;
        this.description = description;
        this.address = address;
        this.businessNumber = businessNumber;
        this.phoneNumber = phoneNumber;
        this.restID = restID;
    }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getAddress() {return address;}
    public String getBusinessNumber() {return businessNumber; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getImageurl() { return imageurl; }

    public String getRestID() {
        return restID;
    }

    public void setRestID(String restID) {
        this.restID = restID;
    }


    // TODO: addFood method
    // TODO: removeFood method
    // TODO: Some way to access the food items
}
