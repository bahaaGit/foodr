package ateam.foodr;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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

public class OwnerFoodMenu extends AppCompatActivity implements ChildEventListener,FoodAdapter.OnItemClickListener
{
    private String restaurantKey;

    private ArrayList<Food> foodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private ImageView restImage;
    private FloatingActionButton addFoodBtn;


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

        //adapter.setClickListener(this::onItemClick);
        adapter.setOnItemClickListener(OwnerFoodMenu.this);

        restImage = findViewById(R.id.restImage);
        addFoodBtn = findViewById(R.id.addButton);

        if (!SimplerLoginActivity.user.getUser_type().equals("admin"))
        {
            addFoodBtn.setVisibility(View.INVISIBLE);
        }

        // Subscribe to events so the recycler view gets populated with food items
        DatabaseReference restaurantDB = FirebaseDatabase.getInstance().getReferenceFromUrl(restaurantKey);

        restaurantDB.child("imageurl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null)
                    return;
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
    public void onChildAdded(DataSnapshot snapshot, String s)
    {
        // Add it to the list
        Food allFoods = snapshot.getValue(Food.class); // Deserialize it into a Food object
        foodList.add(allFoods);                        // Add it to the list so the recycler can show it
        adapter.notifyDataSetChanged();         // Let the recycler know to update the list

        Log.d("Adding food child", allFoods.getName());
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



    @Override
    public void onItemClick(int position) {
        Intent createIntent = new Intent(OwnerFoodMenu.this, FoodViewActivity.class);
        createIntent.putExtra("Database Reference",foodList.get(position).getUid());
        startActivity(createIntent);
    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(foodList.get(position).getUid());
        mDatabase.removeValue();
    }

    @Override
    public void onEditClick(int position) {

        // Show the create restaurant page
        Intent createIntent = new Intent(OwnerFoodMenu.this, CreateFoodActivity.class);
        createIntent.putExtra("Database Reference",foodList.get(position).getUid());
        startActivity(createIntent);
    }
}
