package ateam.foodr;

import org.jetbrains.annotations.NotNull;

public class Restaurant
{
    // Boilerplate accessors
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getBusinessNumber() {return businessNumber; }
    public String getPhoneNumber() { return phoneNumber; }

    // Private fields
    private String name;
    private String description;
    private String businessNumber;
    private String phoneNumber;

    // TODO: Location field
    // TODO: Menu array

    /** Standard boilerplate constructor. */
    public Restaurant(String name, String description, String phoneNumber, String businessNumber)
    {
        this.name = name;
        this.businessNumber = businessNumber;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }

    // TODO: addFood method
    // TODO: removeFood method
    // TODO: Some way to access the food items
}
