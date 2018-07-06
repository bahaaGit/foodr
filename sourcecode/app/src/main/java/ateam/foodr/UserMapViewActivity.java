package ateam.foodr;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class UserMapViewActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map_view);

        // Set up the menu in the top right corner
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /* Toolbar stuff */

    //This will get the menu for us
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        //This sticks the menu to the toolbar the name of the file is menu_log.xml so in the menu directory main_menu.xml
        getMenuInflater().inflate(R.menu.menu_log,menu);
        return true;
    }

    //When there is a click on the menu tab
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        //According to which item is clicked
        if (item.getItemId() == R.id.main_logout_btn)
        {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(UserMapViewActivity.this, SimplerLoginActivity.class);

            //Redirect the user to the startActivity view

            startActivity(intent);

            //Make this so that the user can't access the main view through the back button
            finish();
        }

        return true;
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Subscribe to the marker click event
        googleMap.setOnMarkerClickListener(this::onMarkerClick);

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

        // Recenter the map on the user's location
                /*CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(40.76793169992044,
                        -73.98180484771729));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);*/

//        button = findViewById(R.id.MapViewLogOutBtn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//
//                Intent intent = new Intent(UserMapViewActivity.this, SimplerLoginActivity.class);
//
//                //Redirect the user to the startActivity view
//
//                startActivity(intent);
//
//                //Make this so that the user can't access the main view through the back button
//                finish();
//            }
//        });
    }

    private void addRestaurantToMap(Restaurant r)
    {
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
    }

    private boolean onMarkerClick(Marker marker)
    {
        // Get the restaurant from the marker
        Restaurant r = (Restaurant)(marker.getTag());

        // Go to the restaurant's menu.
        Intent menuIntent = new Intent(this, UserFoodMenu.class);
        menuIntent.putExtra(ActivityParams.RESTAURANT_KEY, r.getRestID());

        startActivity(menuIntent);

        // Returning false tells Google Maps to run the default behavior
        // in addition to our logic
        return false;
    }
}
