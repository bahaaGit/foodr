package ateam.foodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class OwnerRestaurantListActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_restaurant_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: Get the currently logged-in user id
    }

    public void onAddButtonClick(View view)
    {
        // Put the user id in a bundle so we can send it to the create page
        Bundle b = new Bundle();
        b.putInt("uid", 0);     // TODO: Put the actual uid in here.

        // Show the create restaurant page
        Intent createIntent = new Intent(OwnerRestaurantListActivity.this, CreateRestaurantActivity.class);
        createIntent.putExtras(b);
        startActivity(createIntent);
    }
}
