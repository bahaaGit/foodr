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
    private RecyclerView recyclerView;
    private ArrayList<Food> foodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_food_menu);

        // Set up the recycler view
        FoodAdapter adapter = new FoodAdapter(this,foodList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(rvLinearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onAddButtonClick(View view)
    {
        Intent createFoodIntent = new Intent(this, CreateFoodActivity.class);
        startActivity(createFoodIntent);
    }
}
