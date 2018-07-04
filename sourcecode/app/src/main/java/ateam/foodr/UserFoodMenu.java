package ateam.foodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserFoodMenu extends AppCompatActivity{ //implements FoodAdapter.OnItemClickListener{

    RecyclerView recyclerView;
    ArrayList<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food_menu);

        // Populate the food list
        foodList = new ArrayList<>();
        foodList.add(new Food("url","Food1","The is good food",0,"hjj", ""));
        foodList.add(new Food("url","Food1","The is good food",0,"ghh", ""));

        // Set up the recycler view
        FoodAdapter adapter = new FoodAdapter(this,foodList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(rvLinearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    /*@Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {


    }

    @Override
    public void onEditClick(int position) {
        // Show the create restaurant page
        Intent createIntent = new Intent(UserFoodMenu.this, CreateFoodActivity.class);
        createIntent.putExtra("Database Reference",foodList.get(position).getUid());
        startActivity(createIntent);
    }*/
}
