package ateam.foodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class UserFoodMenu extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food_menu);

        // Populate the food list
        foodList = new ArrayList<>();

        foodList.add(new Food(R.drawable.topmenubar2,"Food1","23","The is good food"));
        foodList.add(new Food(R.drawable.topmenubar2,"Food1","23","The is good food"));

        // Set up the recycler view
        FoodAdapter adapter = new FoodAdapter(this,foodList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(rvLinearLayoutManager);
        recyclerView.setAdapter(adapter);

        // TODO: Hide the "add" button if the user is not an admin
        boolean isAdmin = true;
        if (!isAdmin)
            findViewById(R.id.addButton).setVisibility(View.INVISIBLE);

    }

    public void onAddButtonClick(View v)
    {
        // Take us to the add activity
        Intent createFoodIntent = new Intent(this, CreateFoodActivity.class);
        startActivity(createFoodIntent);
    }
}
