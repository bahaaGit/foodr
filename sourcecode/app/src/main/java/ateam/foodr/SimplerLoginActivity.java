package ateam.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.*;

public class SimplerLoginActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpler_login);
    }

    public void onLoginClick(View view)
    {
        // TODO: validate credentials before logging in

        // If the user marked themselves as an owner, take them to the owner page
        Switch ownerToggle = findViewById(R.id.ownerToggle);
        if (ownerToggle.isChecked())
        {
            Intent ownerPageIntent = new Intent(this, OwnerRestaurantListActivity.class);
            startActivity(ownerPageIntent);
        }

        // TODO: Do something different if the user isn't
        // marked as an owner.
    }
}
