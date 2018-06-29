package ateam.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

public class ResturantsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Restaurant> resturantList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturants);

        recyclerView = findViewById(R.id.resturantsrv);
        resturantList = new ArrayList<>();

        resturantList.add(new Restaurant("url",R.drawable.topmenubar2,"abdul","lawson","uu","hh","b"));
        resturantList.add(new Restaurant("url",R.drawable.topmenubar2,"abdul","lawson","hh","hh","cc"));



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLinearLayoutManager = linearLayoutManager;

        recyclerView.setLayoutManager(rvLinearLayoutManager);

        ResturantsAdapter adapter = new ResturantsAdapter(this,resturantList);

        recyclerView.setAdapter(adapter);

        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        ResturantsAdapter resturantAdapter = new ResturantsAdapter(this,resturantList);
        recyclerView.setAdapter(resturantAdapter);*/

    }
}
