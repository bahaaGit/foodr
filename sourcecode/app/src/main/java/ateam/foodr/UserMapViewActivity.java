package ateam.foodr;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserMapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getAllRestaurants().continueWith((restaurants) -> {

            // Add every restaurant to the map
            Geocoder geocoder = new Geocoder(this);
            for (Restaurant r : restaurants.getResult()){

                // Get the coordinates from the address.
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(r.getAddress(), 1);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);      // Let the exception bubble up
                }

                // Skip this restaurant if the address doesn't map to any coordinates
                if (addresses.size() == 0)
                    continue;

                Address addr = addresses.get(0);
                LatLng coordinates = new LatLng(addr.getLatitude(), addr.getLongitude());

                // Add a marker at that position
                MarkerOptions opts = new MarkerOptions()
                        .position(coordinates)
                        .title(r.getName())
                        .snippet(r.getDescription());

                googleMap.addMarker(opts);
            }

            return null;
        });


    }


    // Misc metods

    /** Gets all restaurants currently in the database */
    private Task<Restaurant[]> getAllRestaurants()
    {
        // TEMPORARY: return a placeholder list
        Restaurant[] restaurants = new Restaurant[]{
                new Restaurant(
                        "",
                        "Alex's Restaurant",
                        "noobs",
                        "220 Lansdowne, Noblesville, IN, 46060",
                        "317-773-4098",
                        "100",
                        "foo"
                )
        };

        return Tasks.call(() -> restaurants);
    }
}
