package ateam.foodr;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class CreateRestaurantActivity extends AppCompatActivity
{
    private int uid;

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

        // Get the currently logged-in user
        Bundle b = getIntent().getExtras();
        uid = b.getInt("uid");
        nameTextbox.setText("" + uid);

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

        // TODO: Submit to firebase

        // Return to the previous activity
        finish();
    }
}
