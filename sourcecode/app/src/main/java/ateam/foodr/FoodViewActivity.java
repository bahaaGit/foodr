package ateam.foodr;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodViewActivity extends AppCompatActivity implements ChildEventListener{
    private String foodKey;
    private ImageView restImage;
    private TextView name, desc;
    private RatingBar rate;
    private ListView foodCommentsList;
    private FloatingActionButton addReviewBtn;
    private ArrayList<Comment> comments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);

        comments = new ArrayList<Comment>();


        foodKey = getIntent().getStringExtra("Database Reference");

        restImage = findViewById(R.id.idFoodImg);
        name = findViewById(R.id.idFoodName);
        desc = findViewById(R.id.idFoodDesc);
        rate = findViewById(R.id.idFoodRatingBar);
        foodCommentsList = findViewById(R.id.idFoodCommentsList);
        addReviewBtn = findViewById(R.id.idFoodAddReview);

        if (SimplerLoginActivity.user != null && SimplerLoginActivity.user.getUser_type() != null
                && SimplerLoginActivity.user.getUser_type().equals("admin"))
        {
            addReviewBtn.setVisibility(View.INVISIBLE);
        }

        ListAdapter listAdapter = new ListAdapter();

        foodCommentsList.setAdapter(listAdapter);

        // Subscribe to events so the recycler view gets populated with food items
        DatabaseReference foodDB = FirebaseDatabase.getInstance().getReferenceFromUrl(foodKey);

        foodDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("imageurl"))
                    return;
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
                comments = foodItem.getComments();
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

    class ListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if (comments == null)
                return 0;
            return comments.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.rl_comments_item,null);
            TextView commenterName, commenterDesc;

            commenterDesc = convertView.findViewById(R.id.idCommenterDesc);
            commenterName = convertView.findViewById(R.id.idCommenterName);
            if(position == 0){
                return convertView;
            }
            commenterName.setText("Commenter");
            commenterDesc.setText(comments.get(position).getCommentTxt());
            return convertView;
        }
    }
}
