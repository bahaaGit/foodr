package ateam.foodr;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

public class LocationCallbackBuilder extends LocationCallback
{
    public interface LocationResultHandler
    {
        void run(LocationResult r);
    }

    public interface LocationAvailabilityHandler
    {
        void run(LocationAvailability a);
    }

    private LocationResultHandler onResult;
    private LocationAvailabilityHandler onAvailability;

    public LocationCallbackBuilder(LocationResultHandler onResult)
    {
        this.onResult = onResult;
    }

    public LocationCallbackBuilder()
    {
        // Set the default handlers to do nothing
        onResult = r -> {};
        onAvailability = a -> {};
    }

    public void setOnResult(LocationResultHandler h)
    {
        this.onResult = h;
    }

    public void setOnAvailability(LocationAvailabilityHandler h)
    {
        this.onAvailability = h;
    }

    @Override
    public void onLocationAvailability(LocationAvailability a)
    {
        onAvailability.run(a);
    }

    @Override
    public void onLocationResult(LocationResult r)
    {
        onResult.run(r);
    }
}
