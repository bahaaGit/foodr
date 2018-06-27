package ateam.foodr;

import android.location.Address;

import org.jetbrains.annotations.NotNull;

public class Restaurant
{
    // Boilerplate accessors
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getAddress() {return address;}
    public String getBusinessNumber() {return businessNumber; }
    public String getPhoneNumber() { return phoneNumber; }

    // Private fields
    private String name;
    private String description;
    private String address;
    private String businessNumber;
    private String phoneNumber;

    // TODO: Menu array

    /** Standard boilerplate constructor. */
    public Restaurant(String name, String description, String address, String phoneNumber, String businessNumber)
    {
        this.name = name;
        this.description = description;
        this.address = address;
        this.businessNumber = businessNumber;
        this.phoneNumber = phoneNumber;
    }

    // TODO: addFood method
    // TODO: removeFood method
    // TODO: Some way to access the food items

}
