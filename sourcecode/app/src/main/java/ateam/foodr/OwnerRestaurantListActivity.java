package ateam.foodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.*;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OwnerRestaurantListActivity extends AppCompatActivity implements ChildEventListener
{
    private RecyclerView recycler;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurants = new ArrayList<>();

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

    public void onItemClick(View v, int pos)
    {
        // View that restaurant's menu
        Intent menuIntent = new Intent(this, UserFoodMenu.class);
        startActivity(menuIntent);
    }
}
