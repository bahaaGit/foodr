package ateam.foodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OwnersResturantsActivity extends AppCompatActivity  implements ChildEventListener {

    private FirebaseUser user;
    private RecyclerView recyclerView;
    private OwnerResturantsAdapter adapter;
    private ArrayList<Restaurant> resturantList = new ArrayList<>();
    private List<String> restaurantKeys  = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owners_resturants);

        initRecyclerView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Subscribe to data-changed events
        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        DatabaseReference restList = userDB.child("Restaurants");
        restList.addChildEventListener(this);
    }

    public void onAddButtonClick(View view)
    {
        // Show the create restaurant page
        Intent createIntent = new Intent(OwnersResturantsActivity.this, CreateRestaurantActivity.class);
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

            Intent intent = new Intent(OwnersResturantsActivity.this, SimplerLoginActivity.class);

            //Redirect the user to the startActivity view

            startActivity(intent);

            //Make this so that the user can't access the main view through the back button
            finish();
        }

        return true;
    }

    private void initRecyclerView()
    {
        // Set up the recycler view
        recyclerView = findViewById(R.id.ownerresturantsrv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(rvLinearLayoutManager);

        // Set up the adapter
        adapter = new OwnerResturantsAdapter(this,resturantList);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this::onItemClick);
        //adapter.setOnItemClickListener(ResturantsActivity.this);
    }

    public void onChildAdded(DataSnapshot snapshot, String prevChildName)
    {
        // Add it to the list
        Restaurant r = snapshot.getValue(Restaurant.class);
        resturantList.add(r);
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

    @Override
    public void onCancelled(DatabaseError databaseError) {

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
