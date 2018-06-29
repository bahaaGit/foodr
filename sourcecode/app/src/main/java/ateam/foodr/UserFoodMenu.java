package ateam.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class UserFoodMenu extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food_menu);

        recyclerView = findViewById(R.id.rv);

        foodList = new ArrayList<>();

        foodList.add(new Food(R.drawable.topmenubar2,"Food1","The is good food",4));
        foodList.add(new Food(R.drawable.topmenubar2,"Food1","The is good food",23));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;

        recyclerView.setLayoutManager(rvLinearLayoutManager);

        FoodAdapter adapter = new FoodAdapter(this,foodList);

        recyclerView.setAdapter(adapter);

    }
}



   /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false);

    recyclerView.setLayoutManager(linearLayoutManager);

        ResturantAdapter resturantAdapter = new ResturantAdapter(this,resturantList);
        recyclerView.setAdapter(resturantAdapter);*/