package ateam.foodr;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserMapViewActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map_view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


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
        Intent menuIntent = new Intent(this, OwnerFoodMenu.class);
        menuIntent.putExtra(ActivityParams.RESTAURANT_KEY, r.getRestID());

        startActivity(menuIntent);

        // Returning false tells Google Maps to run the default behavior
        // in addition to our logic
        return false;
    }
}
