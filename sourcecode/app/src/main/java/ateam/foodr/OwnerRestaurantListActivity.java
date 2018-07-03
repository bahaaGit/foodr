package ateam.foodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.*;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class OwnerRestaurantListActivity extends AppCompatActivity implements ChildEventListener
{
    private RecyclerView recycler;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<String> restaurantKeys  = new ArrayList<>();   // HACK: Storing the keys in a parallel array
    private FirebaseUser user;                                                            // so we can pass them by reference to
                                                                // another activity

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_restaurant_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the recycler view
        initRecyclerView();

        // Subscribe to data-changed events
        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        DatabaseReference restaurantList = userDB.child("Restaurants");
        restaurantList.addChildEventListener(this);
    }

    private void initRecyclerView()
    {
        // Get the recycler view
        View content = findViewById(android.R.id.content);
        recycler = content.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // Set up the adapter
        adapter = new RestaurantAdapter(this, restaurants);
        adapter.setClickListener(this::onItemClick);
        recycler.setAdapter(adapter);
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
        if (item.getItemId() == R.id.main_logout_btn) {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(OwnerRestaurantListActivity.this, SimplerLoginActivity.class);

            //Redirect the user to the startActivity view

            startActivity(intent);

            //Make this so that the user can't access the main view through the back button
            finish();
        }

        return true;
    }
    public void onCancelled(DatabaseError error)
    {
        // TODO: Stuff

    }

    public void onChildAdded(DataSnapshot snapshot, String prevChildName)
    {
        // Add it to the list
        Restaurant r = snapshot.getValue(Restaurant.class);
        restaurants.add(r);
        adapter.notifyDataSetChanged();

        // HACK: Save the restaurant's key so we can pass it to other activities
        restaurantKeys.add(snapshot.getRef().toString());
    }

    public void onChildChanged(DataSnapshot snapshot, String prevChildName)
    {

    }

    public void onChildMoved(DataSnapshot snapshot, String prevChildName)
    {

    }

    public void onChildRemoved(DataSnapshot snapshot) {

    }

    public void onItemClick(View v, int pos)
    {
        // View that restaurant's menu
        Intent menuIntent = new Intent(this, OwnerFoodMenu.class);

        // HACK: Pass the restaurant's key to the next activity so it knows what food to get.
        menuIntent.putExtra(ActivityParams.RESTAURANT_KEY, restaurantKeys.get(pos));

        startActivity(menuIntent);
    }
}

