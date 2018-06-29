package ateam.foodr;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateFoodActivity extends AppCompatActivity
{
    @BindView(R.id.nameTextbox)  EditText nameTextbox;
    @BindView(R.id.priceTextbox) EditText priceTextbox;
    @BindView(R.id.descTextbox)  EditText descTextbox;

    @BindView(R.id.photoButton)  Button photoButton;
    @BindView(R.id.createButton) Button createButton;

    private String restaurantKey;
    private FirebaseUser mCurrentUser;

    //The storage reference so that the profile images can be stored on the FireBase
    private StorageReference mImageStorage;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food);
        ButterKnife.bind(this);

        //The current user instance that has been logged in
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Get the storage reference so that the profile images can be saved to the FireBase
        mImageStorage = FirebaseStorage.getInstance().getReference();


        // Get the restaurant key parameter
        restaurantKey = getIntent().getStringExtra(ActivityParams.RESTAURANT_KEY);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This code is to select a image or something
                //You initialize the intent
                Intent galleryIntent = new Intent();

                //You select the intent type
                galleryIntent.setType("image/*");

                //Specify that through this you want to get the content
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                //With this you start the Intent
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),1);

            }
        });

    }

    //This will run when the image is selected for the user
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


                Uri imageUri = data.getData();

                //According to the id of the user save the user image inside the profile_images directory
                String uid = mCurrentUser.getUid();

                //Access the location where you are going to save the profile picture
                StorageReference storage = mImageStorage.child("food_images").child(uid);

                //Put the file onto the directory and do some tasks when the task is done
                storage.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            //We will need the download URL to get it later on so we need to store this
                            String download_url = task.getResult().getDownloadUrl().toString();

                            url = download_url;

                            //To tell the user that this is done
                            Toast.makeText(CreateFoodActivity.this, "The image is updated", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(CreateFoodActivity.this, "There was an error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void onCreateButtonClick(View v)
    {
        // Take all the information from the form and put it in an object
        if (url == null)
            url = "empty";
        Food f = new Food
        (
                url,     // TODO: Fill this in
                nameTextbox.getText().toString(),
                priceTextbox.getText().toString(),     // TODO: Make this an int
                descTextbox.getText().toString()
        );

        // Send that object to firebase
        DatabaseReference restaurant = FirebaseDatabase.getInstance().getReferenceFromUrl(restaurantKey);
        DatabaseReference foodList = restaurant.child("FoodMenu");
        DatabaseReference newFood = foodList.push();

        newFood.setValue(f)
                // Show an error message on failure
                .addOnFailureListener( (Exception e) -> Utils.showToast(this, e.getMessage()) )

                // Finish the activity on success
                .addOnSuccessListener( (task) -> finish() );
    }

}
