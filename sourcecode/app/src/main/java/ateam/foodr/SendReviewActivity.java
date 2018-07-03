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

public class SendReviewActivity extends AppCompatActivity {

    //RatingBar mRatingBar = (RatingBar) findViewById(R.id.idReviewRatingBar);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_review);

        RatingBar reviewRatingBar = (RatingBar)findViewById(R.id.idReviewRatingBar);
        TextView reviewRatingScale = (TextView) findViewById(R.id.idReviewRatingScale);
        EditText feedback = (EditText) findViewById(R.id.idReviewDesc);
        Button mSendFeedback = (Button) findViewById(R.id.idReviewfeedBackBtn);

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
                    feedback.setText("");
                    reviewRatingBar.setRating(0);
                    Toast.makeText(SendReviewActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
