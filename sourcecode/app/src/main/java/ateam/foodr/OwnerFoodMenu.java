package ateam.foodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class OwnerFoodMenu extends AppCompatActivity
{
    public static final String RESTAURANT_KEY = "restaurantKey";

    private String restaurantKey;

    private RecyclerView recyclerView;
    private ArrayList<Food> foodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_food_menu);

        // Get the restaurant key as a parameter
        restaurantKey = getIntent().getStringExtra(RESTAURANT_KEY);
        Utils.showToast(this, restaurantKey);

        // Set up the recycler view
        FoodAdapter adapter = new FoodAdapter(this,foodList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(rvLinearLayoutManager);
        recyclerView.setAdapter(adapter);

        // TODO: Populate the food list from Firebase
    }

    public void onAddButtonClick(View view)
    {
        Intent createFoodIntent = new Intent(this, CreateFoodActivity.class);
        startActivity(createFoodIntent);
    }
}
