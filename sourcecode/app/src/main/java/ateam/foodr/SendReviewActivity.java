package ateam.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SendReviewActivity extends AppCompatActivity {
    private String reference;
    private RatingBar reviewRatingBar;
    private TextView reviewRatingScale;
    private  EditText feedback;
    private Button mSendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_review);



        reference  =  getIntent().getStringExtra("Database Reference");
        reviewRatingBar = (RatingBar)findViewById(R.id.idReviewRatingBar);
        reviewRatingScale = (TextView) findViewById(R.id.idReviewRatingScale);
        feedback = (EditText) findViewById(R.id.idReviewDesc);
        mSendFeedback = (Button) findViewById(R.id.idReviewfeedBackBtn);

        reviewRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        reviewRatingScale.setText("Very bad");
                        break;
                    case 2:
                        reviewRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        reviewRatingScale.setText("Good");
                        break;
                    case 4:
                        reviewRatingScale.setText("Great");
                        break;
                    case 5:
                        reviewRatingScale.setText("Awesome. I love it");
                        break;
                    default:
                        reviewRatingScale.setText("");
                }
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedback.getText().toString().isEmpty()) {
                    Toast.makeText(SendReviewActivity.this, "Please fill in feedback text box", Toast.LENGTH_LONG).show();
                } else {
                    feedback.getText();
                    reviewRatingBar.setRating(0);
                    Toast.makeText(SendReviewActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!reference.isEmpty()){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(reference);

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Food foodItem = dataSnapshot.getValue(Food.class) ;
                            foodItem.addComments(feedback.toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                Toast.makeText(SendReviewActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
