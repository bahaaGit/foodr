package ateam.foodr;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class CreateRestaurantActivity extends AppCompatActivity
{
    private EditText nameTextbox;
    private EditText businessIDFirstTextbox;
    private EditText businessIDSecondTextbox;   // TODO: Just make this a single textbox.
    private EditText addressTextbox;
    private EditText phoneTextbox;


    // Event Handlers

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        // Get the textboxes
        // Holy boilerplate, batman!
        nameTextbox =    findViewById(R.id.nameTextbox);
        addressTextbox = findViewById(R.id.addressTextbox);
        phoneTextbox =   findViewById(R.id.phoneTextbox);

        businessIDFirstTextbox  =   findViewById(R.id.businessIDFirstTextbox);
        businessIDSecondTextbox =   findViewById(R.id.businessIDSecondTextbox);

    }

    public void onCreateClick(View view)
    {
        // Get all of the information we need from the textboxes
        String name = nameTextbox.getText().toString();
        String businessID = businessIDFirstTextbox.getText().toString();
        businessID += businessIDSecondTextbox.getText().toString();

        String address = addressTextbox.getText().toString();
        String phoneNumber = phoneTextbox.getText().toString();

        // Put that information into a Restaurant object
        Restaurant r = new Restaurant(name, "", address, phoneNumber, businessID);

        // Get the user's restaurant list so we can add this restaurant to it.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        DatabaseReference restaurantList = userDB.child("Restaurants");

        // Send it to the database
        restaurantList.setValue(r.toHashMap())

                // If it failed, show an error message
                .addOnFailureListener((task) -> Utils.showToast(this, "Error"))

                // If it succeeded, return to the previous Activity
                .addOnSuccessListener((task) -> finish());
    }

    // Misc methods
}
