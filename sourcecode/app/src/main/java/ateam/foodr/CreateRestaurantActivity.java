package ateam.foodr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CreateRestaurantActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_IMAGE_REQUEST = 2;

    private EditText nameTextbox;
    private EditText businessIDFirstTextbox;
    private EditText businessIDSecondTextbox; // TODO: Just make this a single textbox.
    private EditText addressTextbox;
    private EditText phoneTextbox;
    private Button chooseImageBtn;
    private Button uploadImageButton;
    private Uri imageUri;
    private ImageView choosenImageView;
    private StorageReference mImageStorage;
    private String url;
    private String restaurantKey;
    private FirebaseUser mCurrentUser;

    // Event Handlers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        // Get the textboxes
        // Holy boilerplate, batman!
        nameTextbox = findViewById(R.id.nameTextbox);
        addressTextbox = findViewById(R.id.addressTextbox);
        phoneTextbox = findViewById(R.id.phoneTextbox);
        chooseImageBtn = findViewById(R.id.idCRChooseImage);
        businessIDFirstTextbox = findViewById(R.id.businessIDFirstTextbox);
        businessIDSecondTextbox = findViewById(R.id.businessIDSecondTextbox);
        choosenImageView = findViewById(R.id.idCRImageView);
        uploadImageButton = findViewById(R.id.idAddRestPhotoSummitBtn);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        //Get the storage reference so that the profile images can be saved to the FireBase
        mImageStorage = FirebaseStorage.getInstance().getReference();

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_IMAGE_REQUEST);
            }
        });

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }

    public void onCreateClick(View view) {
        // Get all of the information we need from the textboxes
        String name = nameTextbox.getText().toString();
        String businessID = businessIDFirstTextbox.getText().toString();
        businessID += businessIDSecondTextbox.getText().toString();
        String address = addressTextbox.getText().toString();
        String phoneNumber = phoneTextbox.getText().toString();

        if (url == null)
            url = "empty";

        // Put that information into a Restaurant object
        Restaurant r = new Restaurant(url, name, "blank description", address, phoneNumber, businessID);

        // Get the user's restaurant list so we can add this restaurant to it.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        DatabaseReference restaurantList = userDB.child("Restaurants");

        // Send it to the database
        DatabaseReference newRestaurant = restaurantList.push();
        newRestaurant.setValue(r)

                // If it failed, show an error message
                .addOnFailureListener((Exception e) -> Utils.showToast(this, e.getMessage()))

                // If it succeeded, return to the previous Activity
                .addOnSuccessListener((task) -> finish());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(choosenImageView);
            //According to the id of the user save the user image inside the profile_images directory
            String uid = mCurrentUser.getUid();
            //Access the location where you are going to save the profile picture
            StorageReference storage = mImageStorage.child("restaurant_images").child(imageUri.getLastPathSegment());

            //Put the file onto the directory and do some tasks when the task is done
            storage.putFile(imageUri).addOnCompleteListener(new OnCompleteListener < UploadTask.TaskSnapshot > () {
                @Override
                public void onComplete(@NonNull Task < UploadTask.TaskSnapshot > task) {
                    if (task.isSuccessful()) {
                        //We will need the download URL to get it later on so we need to store this
                        String download_url = task.getResult().getDownloadUrl().toString();
                        url = download_url;
                        //To tell the user that this is done
                        Toast.makeText(CreateRestaurantActivity.this, "The image is updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CreateRestaurantActivity.this, "There was an error!", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }

        if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBAOS = baos.toByteArray();

            StorageReference storage;
            storage = mImageStorage.child("restaurant_images").child("data" + new Date().getTime());

            //Put the picture selected to the display
            choosenImageView.setImageBitmap(bitmap);

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
                        Toast.makeText(CreateRestaurantActivity.this, "The image is updated", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(CreateRestaurantActivity.this, "There was an error!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}