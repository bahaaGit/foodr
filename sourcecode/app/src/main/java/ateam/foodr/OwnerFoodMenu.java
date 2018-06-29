package ateam.foodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OwnerFoodMenu extends AppCompatActivity implements ChildEventListener
{
    private String restaurantKey;

    private ArrayList<Food> foodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FoodAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_food_menu);

        // Get the restaurant key as a parameter
        restaurantKey = getIntent().getStringExtra(ActivityParams.RESTAURANT_KEY);

        // Set up the recycler view
        adapter = new FoodAdapter(this,foodList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(rvLinearLayoutManager);
        recyclerView.setAdapter(adapter);

        // Subscribe to events so the recycler view gets populated with food items
        DatabaseReference restaurantDB = FirebaseDatabase.getInstance().getReferenceFromUrl(restaurantKey);
        DatabaseReference foodDB = restaurantDB.child("FoodMenu");

        foodDB.addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String s)
    {
        // Add it to the list

        Food f = snapshot.getValue(Food.class); // Deserialize it into a Food object
        foodList.add(f);                        // Add it to the list so the recycler can show it
        adapter.notifyDataSetChanged();         // Let the recycler know to update the list

        Log.d("Adding food child", f.getName());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot)
    {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onCancelled(DatabaseError databaseError)
    {

    }

    public void onAddButtonClick(View view)
    {
        Intent createFoodIntent = new Intent(this, CreateFoodActivity.class);
        createFoodIntent.putExtra(ActivityParams.RESTAURANT_KEY, restaurantKey);

        startActivity(createFoodIntent);
    }
}
