package ateam.foodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class SendReviewActivity extends AppCompatActivity {
    private String reference;
    private RatingBar reviewRatingBar;
    private TextView reviewRatingScale;
    private  EditText feedback;
    private Button mSendFeedback;
    private String foodKey;
    public double rating;
    public double ratingTotal;
    public double numberOfRating;
    public double rate;
    public int go;
    DatabaseReference mDatabase;
    DatabaseReference usrRef;
    String usr;
    Food foodItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_review);

        //Check if a user is logged in
        //FireBase Authentication Linker
        //private FirebaseAuth mAuth;
        //FirebaseUser currentUser = mAuth.getCurrentUser();

//        if (getIntent().getStringExtra("Position") != null)
//        {
//            position = Integer.parseInt(getIntent().getStringExtra("Position"));
//        }
//        else
//            {
//                position = -1;
//            }

        foodKey  =  getIntent().getStringExtra("Database Reference");
        reference = foodKey;
        reviewRatingBar = (RatingBar)findViewById(R.id.idReviewRatingBar);
        reviewRatingScale = (TextView) findViewById(R.id.idReviewRatingScale);
        feedback = (EditText) findViewById(R.id.idReviewDesc);
        mSendFeedback = (Button) findViewById(R.id.idReviewfeedBackBtn);


        if (foodKey != null)
        {
            mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(foodKey);

        }
        rating = 0.1;
        go = 0;
        String key = "https://foodr-6f6d1.firebaseio.com/Users/"+FirebaseAuth.getInstance().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(foodKey);
        usrRef = FirebaseDatabase.getInstance().getReferenceFromUrl(key);

        reviewRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        rating = 1;
                        reviewRatingScale.setText("Very bad");
                        break;
                    case 2:
                        rating = 2;
                        reviewRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        rating = 3;
                        reviewRatingScale.setText("Good");
                        break;
                    case 4:
                        rating = 4;
                        reviewRatingScale.setText("Great");
                        break;
                    case 5:
                        rating = 5;
                        reviewRatingScale.setText("Awesome. I love it");
                        break;
                    default:
                        rating = 0.1;
                        reviewRatingScale.setText("");
                }
                mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ratingTotal = Double.parseDouble(dataSnapshot.child("totalOfRating").getValue().toString());
                numberOfRating = Double.parseDouble(dataSnapshot.child("numOfRating").getValue().toString());
                foodItem = dataSnapshot.getValue(Food.class);
                go = 1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });
                usrRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         usr = dataSnapshot.child("user_name").getValue().toString();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            if (go == 1) {
                rate = (ratingTotal + rating) / (numberOfRating + 1.0);
                mDatabase.child("rate").setValue(rate);
                mDatabase.child("totalOfRating").setValue(ratingTotal + rating);
                mDatabase.child("numOfRating").setValue(numberOfRating + 1.0);
                go = 0;
            }
            }
        });


        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedback.getText().toString().isEmpty()) {
                    Toast.makeText(SendReviewActivity.this, "Please fill in feedback text box", Toast.LENGTH_LONG).show();
                } else {
                    if (FoodViewActivity.position != -1 && foodItem.comments.get(FoodViewActivity.position + 1).getComentId().equals(FirebaseAuth.getInstance().getUid())){

                        rate = (ratingTotal + rating - foodItem.comments.get(FoodViewActivity.position + 1).getRating())
                                / (numberOfRating + 0.0);
                        mDatabase.child("rate").setValue(rate);
                        mDatabase.child("totalOfRating").setValue(ratingTotal + rating - foodItem.comments.get(FoodViewActivity.position + 1).getRating());
                        mDatabase.child("numOfRating").setValue(numberOfRating);
                        //reviewRatingBar.setRating((float) rate);
                        Date date = new Date();

                        // display time and date
                        String sTimeNow = String.format("%tc", date);
                        Comment commnt = new Comment(FirebaseAuth.getInstance().getUid(),usr,feedback.getText().toString(), sTimeNow, rating);

                        // foodItem.comments.remove(position);
                        foodItem.comments.set(FoodViewActivity.position + 1, commnt);
                        mDatabase.child("comments").setValue(foodItem.comments);
                        Toast.makeText(SendReviewActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();

                        Intent createIntent = new Intent(SendReviewActivity.this, UserFoodMenu.class);
                        //createIntent.putExtra("Database Reference",foodKey);
                        createIntent.putExtra(ActivityParams.RESTAURANT_KEY, UserMapViewActivity.selectedRestaurant.getRestID());
                        createIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(createIntent);
                        finish();


                    } else
                        {

                            rate = (ratingTotal + rating) / (numberOfRating + 1.0);
                            mDatabase.child("rate").setValue(rate);
                            mDatabase.child("totalOfRating").setValue(ratingTotal + rating);
                            mDatabase.child("numOfRating").setValue(numberOfRating + 1.0);
                            //reviewRatingBar.setRating((float) rate);
                            Date date = new Date();

                            // display time and date
                            String sTimeNow = String.format("%tc", date);
                            Comment commnt = new Comment(FirebaseAuth.getInstance().getUid(),usr,feedback.getText().toString(), sTimeNow, rating);

                            foodItem.comments.add(commnt);
                            mDatabase.child("comments").setValue(foodItem.comments);
                            Toast.makeText(SendReviewActivity.this, "Thank you for sharing your feedback!", Toast.LENGTH_SHORT).show();

                            Intent createIntent = new Intent(SendReviewActivity.this, UserFoodMenu.class);
                            createIntent.putExtra(ActivityParams.RESTAURANT_KEY, UserMapViewActivity.selectedRestaurant.getRestID());
                            createIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(createIntent);
                            finish();

                        }



                }
            }
        });
    }

}
