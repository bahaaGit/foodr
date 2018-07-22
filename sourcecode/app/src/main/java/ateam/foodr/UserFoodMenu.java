package ateam.foodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserFoodMenu extends AppCompatActivity implements ChildEventListener, UserFoodMenuAdapter.OnItemClickListener{

    RecyclerView recyclerView;
    ArrayList<Food> foodList  = new ArrayList<>();
    private String restaurantKey;
    UserFoodMenuAdapter adapter;
    private ImageView restImage;
    private android.support.v7.widget.Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food_menu);

        // Populate the food list
        restaurantKey = getIntent().getStringExtra(ActivityParams.RESTAURANT_KEY);

        toolbar =  findViewById(R.id.toolbarofUserMenu);
        setSupportActionBar(toolbar);

        //This puts the arrow to go back to the view that we set in the AndroidManifest.xml file specificly for this view
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the recycler view
        adapter = new UserFoodMenuAdapter(this,foodList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(rvLinearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(UserFoodMenu.this);
        restImage = findViewById(R.id.idUserFoodMTopImg);


        // Subscribe to events so the recycler view gets populated with food items
        DatabaseReference restaurantDB = FirebaseDatabase.getInstance().getReferenceFromUrl(restaurantKey);
        restaurantDB.child("imageurl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image_URL = dataSnapshot.getValue().toString();
                if (!image_URL.equals("empty"))
                {
                    Picasso.with(restImage.getContext()).load(image_URL).into(restImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference foodDB = restaurantDB.child("FoodMenu");

        foodDB.addChildEventListener(this);
    }

    @Override
    public void onItemClick(int position) {
        // Show the create restaurant page
        Intent createIntent = new Intent(UserFoodMenu.this, FoodViewActivity.class);
        createIntent.putExtra("Database Reference",foodList.get(position).getUid());
        startActivity(createIntent);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        // Add it to the list
        Food allFoods = dataSnapshot.getValue(Food.class); // Deserialize it into a Food object
        foodList.add(allFoods);                        // Add it to the list so the recycler can show it
        adapter.notifyDataSetChanged();         // Let the recycler know to update the list

        Log.d("Adding food child", allFoods.getName());
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
}
