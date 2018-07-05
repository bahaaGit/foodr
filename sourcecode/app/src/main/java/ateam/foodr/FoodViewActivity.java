package ateam.foodr;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodViewActivity extends AppCompatActivity implements ChildEventListener{
    private String foodKey;
    private ImageView restImage;
    private TextView name, desc;
    private RatingBar rate;
    private FloatingActionButton addReviewBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);

        foodKey = getIntent().getStringExtra("Database Reference");

        restImage = findViewById(R.id.idFoodImg);
        name = findViewById(R.id.idFoodName);
        desc = findViewById(R.id.idFoodDesc);
        rate = findViewById(R.id.idFoodRatingBar);
        addReviewBtn = findViewById(R.id.idFoodAddReview);


        // Subscribe to events so the recycler view gets populated with food items
        DatabaseReference foodDB = FirebaseDatabase.getInstance().getReferenceFromUrl(foodKey);

        foodDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image_URL = dataSnapshot.child("imageurl").getValue().toString();
                if (!image_URL.equals("empty"))
                {
                    Picasso.with(restImage.getContext()).load(image_URL).into(restImage);
                }
                //dataSnapshot.child("name").getValue().toString();
                Food foodItem = dataSnapshot.getValue(Food.class) ;

                name.setText(foodItem.getName());
                desc.setText(foodItem.getDesc());
                rate.setRating(foodItem.getRate());
                rate.setEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the create restaurant page
                Intent createIntent = new Intent(FoodViewActivity.this, SendReviewActivity.class);
                createIntent.putExtra("Database Reference",foodKey);
                startActivity(createIntent);
            }
        });
        //DatabaseReference foodDB = restaurantDB.child("FoodMenu");
        //foodDB.addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
