package ateam.foodr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateFoodActivity extends AppCompatActivity
{
    @BindView(R.id.nameTextbox)  EditText nameTextbox;
    @BindView(R.id.descTextbox)  EditText descTextbox;
    @BindView(R.id.photoButton)  Button photoButton;
    @BindView(R.id.idAddFoodSummitBtn2) Button takePhoto;
    @BindView(R.id.createButton) Button createButton;
    @BindView(R.id.idAddFoodcChoosenImageView2) ImageView photoView;

    private String restaurantKey;
    private FirebaseUser mCurrentUser;

    //The storage reference so that the profile images can be stored on the FireBase
    private StorageReference mImageStorage;
    private String url;
    public String reference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food);
        ButterKnife.bind(this);

        reference  =  getIntent().getStringExtra("Database Reference");

        if (reference != null)
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(reference);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nameTextbox.setText(dataSnapshot.child("name").getValue().toString());
                    descTextbox.setText(dataSnapshot.child("desc").getValue().toString());
                    url = dataSnapshot.child("imageurl").getValue().toString();
                    if (!url.equals("empty"))
                    {
                        Picasso.with(photoView.getContext()).load(dataSnapshot.child("imageurl").getValue().toString()).into(photoView);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        //The current user instance that has been logged in
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Get the storage reference so that the profile images can be saved to the FireBase
        mImageStorage = FirebaseStorage.getInstance().getReference();


        // Get the restaurant key parameter
        restaurantKey = getIntent().getStringExtra(ActivityParams.RESTAURANT_KEY);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);

            }
        });

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

        if (requestCode == 1 && resultCode == RESULT_OK){

                Uri imageUri = data.getData();

                //According to the id of the user save the user image inside the profile_images directory
                String uid = mCurrentUser.getUid();

                Random rand = new Random();
                int  n = rand.nextInt(1000000000);
                StorageReference storage = mImageStorage.child("restaurant_images").child(Integer.toString(n));

                //Put the picture selected to the display
                Picasso.with(this).load(imageUri).into(photoView);

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

        if (requestCode == 2 && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBAOS = baos.toByteArray();

            StorageReference storage;
            storage = mImageStorage.child("food_images").child("data" + new Date().getTime());

            //Put the picture selected to the display
            photoView.setImageBitmap(bitmap);

            //Put the file onto the directory and do some tasks when the task is done
            storage.putBytes(dataBAOS).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
    }


    public void onCreateButtonClick(View v)
    {
        // Take all the information from the form and put it in an object
        if (url == null)
            url = "empty";

        // Send that object to firebase

        DatabaseReference newFood;
        if (reference != null)
        {
           newFood = FirebaseDatabase.getInstance().getReferenceFromUrl(reference);
        }
        else
            {
                DatabaseReference restaurant = FirebaseDatabase.getInstance().getReferenceFromUrl(restaurantKey);
                DatabaseReference foodList = restaurant.child("FoodMenu");
                newFood = foodList.push();
            }
        ArrayList<String> newcommt = new ArrayList<>();

        newcommt.add("");


        Food f = new Food
                (
                        newFood.getRef().toString(),     // TODO: Fill this in
                        nameTextbox.getText().toString(),
                        descTextbox.getText().toString(),0,0,0,url,newcommt);

        newFood.setValue(f)
                // Show an error message on failure
                .addOnFailureListener( (Exception e) -> Utils.showToast(this, e.getMessage()) )

                // Finish the activity on success
                .addOnSuccessListener( (task) -> finish() );
    }

}
