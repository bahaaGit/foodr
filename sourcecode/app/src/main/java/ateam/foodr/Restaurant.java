package ateam.foodr;

import org.jetbrains.annotations.NotNull;

public class Restaurant
{
    public String name;
    public String description;

    public String businessNumber;
    public String phoneNumber;

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
