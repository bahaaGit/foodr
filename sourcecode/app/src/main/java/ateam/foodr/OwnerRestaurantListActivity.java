package ateam.foodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.*;
import com.google.firebase.database.*;

import java.util.Map;

public class OwnerRestaurantListActivity extends AppCompatActivity implements ChildEventListener
{
    private LinearLayout list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_restaurant_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the list view
        View content = findViewById(android.R.id.content);
        list = content.findViewById(R.id.list);

        // Subscribe to data-changed events
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        DatabaseReference restaurantList = userDB.child("Restaurants");
        restaurantList.addChildEventListener(this);
    }

    public void onAddButtonClick(View view)
    {
        // Show the create restaurant page
        Intent createIntent = new Intent(OwnerRestaurantListActivity.this, CreateRestaurantActivity.class);
        startActivity(createIntent);
    }

    public void onCancelled(DatabaseError error)
    {
        // TODO: Stuff
    }

    public void onChildAdded(DataSnapshot snapshot, String prevChildName)
    {
        // Add a textview to the list
        // TODO: Make it a button or something
        TextView tv = new TextView(this);
        list.addView(tv);

        // Associate the textview with the Restaurant object
        Restaurant r = snapshot.getValue(Restaurant.class);
        tv.setTag(r);
        tv.setText(r.getName());
    }

    public void onChildChanged(DataSnapshot snapshot, String prevChildName)
    {

    }

    public void onChildMoved(DataSnapshot snapshot, String prevChildName)
    {

    }

    public void onChildRemoved(DataSnapshot snapshot)
    {

    }
}
