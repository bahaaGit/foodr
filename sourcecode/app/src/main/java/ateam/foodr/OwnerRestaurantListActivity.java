package ateam.foodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OwnerRestaurantListActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_restaurant_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onAddButtonClick(View view)
    {
        // Show the create restaurant page
        Intent createIntent = new Intent(OwnerRestaurantListActivity.this, CreateRestaurantActivity.class);
        startActivity(createIntent);
    }

    //This will get the menu for us
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //This sticks the menu to the toolbar the name of the file is menu_log.xml so in the menu directory main_menu.xml
        getMenuInflater().inflate(R.menu.menu_log,menu);
        return true;

    }

    //When there is a click on the menu tab
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        //According to which item is clicked
        if (item.getItemId() == R.id.main_logout_btn)
        {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(OwnerRestaurantListActivity.this,SimplerLoginActivity.class);

            //Redirect the user to the startActivity view

            startActivity(intent);

            //Make this so that the user can't access the main view through the back button
            finish();
        }

        return true;
    }
}

