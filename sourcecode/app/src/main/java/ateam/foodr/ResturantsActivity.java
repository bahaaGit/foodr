package ateam.foodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class ResturantsActivity extends AppCompatActivity implements ResturantsAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    ResturantsAdapter adapter;
    ArrayList<Restaurant> resturantList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturants);


        // Set up the recycler view
        recyclerView = findViewById(R.id.resturantsrv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(rvLinearLayoutManager);

        // Set up the adapter
        adapter = new ResturantsAdapter(this, resturantList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(ResturantsActivity.this);

        // For every admin, add all of their restaurants to the list
        // TODO: This is bad code clean it up
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("Users");
        users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Ignore this if it's not an admin
                Log.d("fuck", dataSnapshot.toString() + "\n");
                if (!dataSnapshot.hasChild("Restaurants"))
                    return;

                // Listen to all of its child restaurants
                DatabaseReference userRestaurants = dataSnapshot.getRef().child("Restaurants");
                userRestaurants.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        // Add this restaurant to the list
                        Restaurant r = null;
                        try {
                            Log.d("Restaurant snapshot: ", dataSnapshot.toString());
                            r = dataSnapshot.getValue(Restaurant.class);

                        } catch (Exception e) {
                            // TODO: This is a big no-no, but we're in a hurry.
                            return;
                        }
                        resturantList.add(r);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"Normal Click at position: " + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this,"What click at postion: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(this,"Delete Click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(int position) {
        Toast.makeText(this,"Eit Click at position: " + position, Toast.LENGTH_SHORT).show();
    }
}
