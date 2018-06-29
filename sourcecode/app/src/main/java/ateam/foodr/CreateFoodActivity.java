package ateam.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateFoodActivity extends AppCompatActivity
{
    @BindView(R.id.nameTextbox)  EditText nameTextbox;
    @BindView(R.id.priceTextbox) EditText priceTextbox;
    @BindView(R.id.descTextbox)  EditText descTextbox;

    @BindView(R.id.photoButton)  Button photoButton;
    @BindView(R.id.createButton) Button createButton;

    private String restaurantKey;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food);
        ButterKnife.bind(this);

        // Get the restaurant key parameter
        restaurantKey = getIntent().getStringExtra(ActivityParams.RESTAURANT_KEY);
    }

    public void onCreateButtonClick(View v)
    {
        // Take all the information from the form and put it in an object
        Food f = new Food
        (
                -1,     // TODO: Fill this in
                nameTextbox.getText().toString(),
                priceTextbox.getText().toString(),     // TODO: Make this an int
                descTextbox.getText().toString()
        );

        // Send that object to firebase
        DatabaseReference restaurant = FirebaseDatabase.getInstance().getReferenceFromUrl(restaurantKey);
        DatabaseReference foodRef = restaurant.push();

        foodRef.setValue(f)
                // Show an error message on failure
                .addOnFailureListener( (Exception e) -> Utils.showToast(this, e.getMessage()) )

                // Finish the activity on success
                .addOnSuccessListener( (task) -> finish() );
    }
}
