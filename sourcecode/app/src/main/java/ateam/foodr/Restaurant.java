package ateam.foodr;

import android.location.Address;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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

    /**
     *  Returns a hashmap representation for this object.
     */
    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("description", description);
        map.put("address", address);
        map.put("businessNumber", businessNumber);
        map.put("phoneNumber", phoneNumber);
        // TODO: food array?

        return map;
    }
}
