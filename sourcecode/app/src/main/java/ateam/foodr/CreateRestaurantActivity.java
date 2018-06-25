package ateam.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreateRestaurantActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
    }

    public void onCreateClick(View view)
    {
        // Finish the activity
        // TODO: Logic for actually submitting the form
        finish();
    }
}
