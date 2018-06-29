package ateam.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class ResturantsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ResturantsAdapter adapter;

    ArrayList<Restaurant> resturantList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturants);

        // Set up the recycler view
        recyclerView = findViewById(R.id.resturantsrv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(rvLinearLayoutManager);

        // Set up the adapter
        adapter = new ResturantsAdapter(this,resturantList);
        recyclerView.setAdapter(adapter);

        // For every admin, add all of their restaurants to the list
        // TODO: This is bad code clean it up
        DatabaseReference users = FirebaseDatabase.getInstance().getReference();
        users.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                // Ignore this if it's not an admin
                Map<String, Object> user = dataSnapshot.getValue(Map.class);
                if (!user.get("user_type").equals("admin"))
                    return;

                // Listen to all of its child restaurants
                DatabaseReference userRestaurants = dataSnapshot.getRef().child("Restaurants");
                userRestaurants.addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        // Add this restaurant to the list
                        Restaurant r = dataSnapshot.getValue(Restaurant.class);
                        resturantList.add(r);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }


}
