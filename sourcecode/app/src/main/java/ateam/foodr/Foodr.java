package ateam.foodr;

import android.app.Application;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Foodr extends Application{

    //private DatabaseReference mDatabaseRef;

    //add Firebase Database stuff
    private FirebaseDatabase mDatabaseRef ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabaseRef = FirebaseDatabase.getInstance();
        myRef = mDatabaseRef.getReference("user");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //getData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.");
            }
        });
    }

    private void getData(DataSnapshot dataSnapshot, ArrayList<Foodi> array ) {

        for(DataSnapshot ds : dataSnapshot.getChildren()){

        }
    }
    // Adds/updates user's entry in the Firebase database
    public void updateFood(String uid, Food FoodData){
        Log.d(TAG, "updateFood: ");
        //System.out.println("USER UPDATE ERROR: \(error?.localizedDescription)");
    }

    public void addFood(String uid, Food FoodData){
        Log.d(TAG, "updateFood: ");
    }
    class Foodi {
        private int rate;
        private String uid,name, desc, Imageurl;
        private String[] comments;
    }

    class Users {
        public Restr[] Restaurants;
        private String uid,name,email, Imageurl;
    }
    class Owner {
        public Restr[] Restaurants;
        private String uid,name,email, Imageurl;
    }

    class Restr {
        private String name,Imageurl,Location,description,businessNumber,phoneNumber;
        private Foodi[] foodi;
    }
}


