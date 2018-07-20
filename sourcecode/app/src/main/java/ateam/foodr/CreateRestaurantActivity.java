package ateam.foodr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class CreateRestaurantActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_IMAGE_REQUEST = 2;

    @BindView(R.id.nameTextbox)     EditText nameTextbox;
    @BindView(R.id.addressTextbox)  EditText addressTextbox;
    @BindView(R.id.gpsButton)       Button gpsButton;
    @BindView(R.id.phoneTextbox)    EditText phoneTextbox;
    @BindView(R.id.idAddFoodTitle3) TextView title;
    @BindView(R.id.idCRChooseImage) Button chooseImageBtn;
    @BindView(R.id.idCRImageView)   ImageView choosenImageView;
    @BindView(R.id.idAddRestPhotoSummitBtn) Button uploadImageButton;

    private Uri imageUri;
    private StorageReference mImageStorage;
    private String url;
    private String restaurantKey;
    private FirebaseUser mCurrentUser;
    private FusedLocationProviderClient fusedLoc;

    public String reference;


    // Event Handlers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        ButterKnife.bind(this);

        fusedLoc = getFusedLocationProviderClient(this);

        reference  =  getIntent().getStringExtra("Database Reference");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        //Get the storage reference so that the profile images can be saved to the FireBase
        mImageStorage = FirebaseStorage.getInstance().getReference();

        if (reference != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(reference);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild("name"))
                        return;
                    nameTextbox.setText(dataSnapshot.child("name").getValue().toString());
                    addressTextbox.setText(dataSnapshot.child("address").getValue().toString());
                    phoneTextbox.setText(dataSnapshot.child("phoneNumber").getValue().toString());
                    title.setText("Edit Restaurant");
                    url = dataSnapshot.child("imageurl").getValue().toString();

                    if (!url.equals("empty")) {

                        Picasso.with(chooseImageBtn.getContext())
                                .load(dataSnapshot.child("imageurl")
                                        .getValue()
                                        .toString())
                                .into(choosenImageView);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });

        }

        // Open the upload activity when the user clicks the upload button
        uploadImageButton.setOnClickListener(v -> {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_IMAGE_REQUEST);
        });

        // Open the file chooser when the user clicks the choose image button
        chooseImageBtn.setOnClickListener(v -> openFileChooser());

        // Automatically fill in the address when the user clicks the GPS button
        gpsButton.setOnClickListener(this::onGpsClick);
    }

    public void onCreateClick(View view) {
        // Get all of the information we need from the textboxes
        String name = nameTextbox.getText().toString();
        String businessID = "0";
        String address = addressTextbox.getText().toString();
        String phoneNumber = phoneTextbox.getText().toString();

        if (url == null)
            url = "empty";

        // Get the user's restaurant list so we can add this restaurant to it.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        DatabaseReference restaurantList = userDB.child("Restaurants");

        // Send it to the database
        DatabaseReference newRestaurant;
        if (reference != null) {
            newRestaurant = FirebaseDatabase.getInstance().getReferenceFromUrl(reference);
            newRestaurant.child("address").setValue(address);
            newRestaurant.child("businessNumber").setValue(businessID);
            newRestaurant.child("imageurl").setValue(url);
            newRestaurant.child("name").setValue(name);
            newRestaurant.child("phoneNumber").setValue(phoneNumber);
            onBackPressed();
            finish();
        }
        else {
            newRestaurant= restaurantList.push();

            String foodID = newRestaurant.getRef().toString();

            // Put that information into a Restaurant object
            Restaurant r = new Restaurant(url, name, "blank description", address, phoneNumber, businessID, foodID);

            newRestaurant.setValue(r)

                // If it failed, show an error message
                .addOnFailureListener((Exception e) -> Utils.showToast(this, e.getMessage()))

                // If it succeeded, return to the previous Activity
                .addOnSuccessListener((task) -> finish());
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void onGpsClick(View v) {

        // Subscribe to a single location update
        LocationRequest req = new LocationRequest()
            .setNumUpdates(1)
            .setMaxWaitTime(5000);
        
        try {
            fusedLoc.requestLocationUpdates(req, new LocationCallbackBuilder(this::onLocationChanged), null);
        }
        catch (SecurityException e) {
            throw new RuntimeException(e);
        }

        // Disable the button and textbox until the location has arrived
        gpsButton.setEnabled(false);
        gpsButton.setText("Getting location...");
        addressTextbox.setEnabled(false);
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
            Random rand = new Random();

            int n = rand.nextInt(1000000000);
            StorageReference storage = mImageStorage.child("restaurant_images").child(Integer.toString(n));

            //Put the file onto the directory and do some tasks when the task is done
            storage.putFile(imageUri).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    //We will need the download URL to get it later on so we need to store this
                    String download_url = task.getResult().getDownloadUrl().toString();
                    url = download_url;
                    //To tell the user that this is done
                    Toast.makeText(CreateRestaurantActivity.this, "The image is updated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateRestaurantActivity.this, "There was an error!", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
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
            storage.putBytes(dataBAOS).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    //We will need the download URL to get it later on so we need to store this
                    String download_url = task.getResult().getDownloadUrl().toString();

                    url = download_url;

                    //To tell the user that this is done
                    Toast.makeText(CreateRestaurantActivity.this, "The image is updated", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(CreateRestaurantActivity.this, "There was an error!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /** Gets called once after the user clicks the GPS button. */
    private void onLocationChanged(LocationResult result) {

        Location loc = result.getLastLocation();

        // Re-enable the textbox and button
        gpsButton.setEnabled(true);
        gpsButton.setText("Choose from GPS");
        addressTextbox.setEnabled(true);

        // Get the address from the location
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses= null;

        try {
            geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 100);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }

        // Tell the user if the address is not found
        if (addresses == null) {
            Utils.showToast(this, "Could not find address for location (" + loc.getLatitude() + ", " + loc.getLongitude());
            return;
        }

        Address addr = addresses.get(0);

        // Combine all of the address lines into one string
        StringBuilder builder = new StringBuilder();
        for (int i = 0; addr.getAddressLine(i) != null; i++) {

            builder.append(addr.getAddressLine(i));
        }

        // Put it in the textbox
        addressTextbox.setText(builder.toString());
    }
}