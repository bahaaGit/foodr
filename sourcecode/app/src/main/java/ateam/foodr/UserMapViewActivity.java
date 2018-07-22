package ateam.foodr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE;

public class UserMapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    // How close you need to be to a restaurant(in meters) for the menu to auto-open
    // It's twice the size of an average restaurant.
    private final float MAX_RESTAURANT_RANGE = 640;

    private GoogleMap mMap;
    private List<Marker> allMakerers = new ArrayList<>();

    @BindView(R.id.sidebar)             View sidebar;
    @BindView(R.id.restaurantNameLabel) TextView restaurantNameLabel;
    @BindView(R.id.restaurantImage)     ImageView restaurantImage;
    @BindView(R.id.descTextbox)         TextView descTextbox;

    private FusedLocationProviderClient fusedLoc;   // Provides us with location updates
    private LocationCallback locationCallback =     // A wrapper around the location callback function
            new LocationCallbackBuilder(this::onLocationChanged);

    private Restaurant lastAutoOpenedRestaurant;    // The last restaurant that was automatically
                                                    // opened via GPS.  We need to remember this
                                                    // so the app doesn't constantly open the same
                                                    // restaurant over and over again.

    private Restaurant selectedRestaurant;  // The restaurant currently displayed in the sidebar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map_view);
        ButterKnife.bind(this);

        // Set up the menu in the top right corner
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLoc = getFusedLocationProviderClient(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Start getting location updates
        try {
            LocationRequest req = new LocationRequest()
                    .setFastestInterval(5000);

            fusedLoc.requestLocationUpdates(req, locationCallback, null);
        }
        catch (SecurityException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        // Stop getting location updates
        // This way we don't continue polling the GPS unnecessarily
        fusedLoc.removeLocationUpdates(locationCallback);
    }

    /* Toolbar stuff */

    //This will get the menu for us
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //This sticks the menu to the toolbar the name of the file is menu_log.xml so in the menu directory main_menu.xml
        getMenuInflater().inflate(R.menu.menu_log, menu);
        return true;
    }

    //When there is a click on the menu tab
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        //According to which item is clicked
        if (item.getItemId() == R.id.main_logout_btn) {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(UserMapViewActivity.this, SimplerLoginActivity.class);

            //Redirect the user to the startActivity view

            startActivity(intent);

            //Make this so that the user can't access the main view through the back button
            finish();
        }

        return true;
    }

    private void onMapCameraMove(int reason) {

        // Don't do this if it's not a user-initiated gesture
        if (reason != REASON_GESTURE)
            return;

        sidebar.setVisibility(View.INVISIBLE);
    }


    /* Map stuff */

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Recenter the map on the user's location
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(new LatLng(40.425869, -86.908066), 13);
        mMap.moveCamera(center);

        if (!(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            mMap.setMyLocationEnabled(true);
        }


        // Subscribe to the marker click event
        googleMap.setOnMarkerClickListener(this::onMarkerClick);

        // Close the sidebar when the user moves on the map
        googleMap.setOnCameraMoveStartedListener(this::onMapCameraMove);

        // Add all restaurants to the map
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("Users");
        users.addChildEventListener(new ChildEventListenerBuilder().whenChildIsAdded((dataSnapshot, s) ->
        {
            // Ignore this if it's not an admin
            if (!dataSnapshot.hasChild("Restaurants"))
                return;

            // Listen to all of its child restaurants
            DatabaseReference userRestaurants = dataSnapshot.getRef().child("Restaurants");
            userRestaurants.addChildEventListener(new ChildEventListenerBuilder().whenChildIsAdded((snapshot, str) ->
            {
                // Add this restaurant to the list
                Restaurant r = null;
                try {
                    r = snapshot.getValue(Restaurant.class);
                } catch (Exception e) {
                    // Let the exception bubble up
                    // TODO: Fix this exception by properly deserializing restaurants with menus
                    throw new RuntimeException(e);
                }

                addRestaurantToMap(r);
            }));
        }));
    }

    private void addRestaurantToMap(Restaurant r) {
        // Get the coordinates from the address.
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(r.getAddress(), 1);
        }
        catch (IOException e) {
            throw new RuntimeException(e);      // Let the exception bubble up
        }

        // Skip this restaurant if the address doesn't map to any coordinates
        if (addresses.size() == 0)
            return;

        Address addr = addresses.get(0);
        LatLng coordinates = new LatLng(addr.getLatitude(), addr.getLongitude());

        // Add a marker at that position
        MarkerOptions opts = new MarkerOptions()
                .position(coordinates)
                .title(r.getName())
                .snippet(r.getDescription());

        Marker marker = mMap.addMarker(opts);

        // Associate the marker with the restaurant object.
        // This way we can know which restaurant page to visit
        // when the user clicks it.
        marker.setTag(r);

        // Add the restaurant to the list so we can
        // easily find the closest one whenever the
        // user moves.
        allMakerers.add(marker);
    }

    private boolean onMarkerClick(Marker marker) {

        // Put the selected restaurant in the sidebar
        Restaurant r = (Restaurant)(marker.getTag());
        openSidebar(r);

        // Returning false tells Google Maps to run the default behavior
        // in addition to our logic
        return false;
    }

    public void onLocationChanged(LocationResult locRes) {
        // TODO: Recenter the map's camera to the current location
        Location userLoc = locRes.getLastLocation();

        // Don't do anything if there are no restaurants
        if (allMakerers.isEmpty())
            return;

        // Find the closest restaurant
        Restaurant closest = null;
        float closestDist = Float.MAX_VALUE;

        for (Marker m : allMakerers) {

            // Find the distance to this restaurant
            // We can't just use the pythagorean theorm because
            // Earth is a globe.  This function take into
            // account Earth's curvature
            float[] distResults = new float[1];

            Location.distanceBetween(
                    userLoc.getLatitude(),
                    userLoc.getLongitude(),
                    m.getPosition().latitude,
                    m.getPosition().longitude,
                    distResults
            );

            float dist = distResults[0];

            // If this restaurant is closer, that the current
            // winner, it becomes the new winner.
            if (dist < closestDist){
                closest = (Restaurant)(m.getTag());
                closestDist = dist;
            }
        }

        // Don't do anything if it's not in the maximum range
        if (closestDist > MAX_RESTAURANT_RANGE)
            return;

        // Don't do anything if we've already auto-opened this restaurant
        if (lastAutoOpenedRestaurant == closest)
            return;

        // It was within range, so auto-open it in the sidebar
        lastAutoOpenedRestaurant = closest;
        openSidebar(closest);
    }

    public void onRestaurantMenuButtonClick(View v) {

        // Open the restaurant's menu in a new activity
        Intent menuIntent = new Intent(this, UserFoodMenu.class);
        menuIntent.putExtra(ActivityParams.RESTAURANT_KEY, selectedRestaurant.getRestID());

        startActivity(menuIntent);
    }

    /** Opens the sidebar and displays the given restaurant */
    private void openSidebar(Restaurant r) {

        sidebar.setVisibility(View.VISIBLE);
        selectedRestaurant = r;

        // Load the restaurant's image into the sidebar
        Picasso.with(this).load(r.getImageurl()).into(restaurantImage);

        // Fill out the textboxes
        restaurantNameLabel.setText(selectedRestaurant.getName());
        descTextbox.setText(r.getDescription());
    }
}
