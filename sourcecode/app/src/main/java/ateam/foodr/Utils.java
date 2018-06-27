package ateam.foodr;

import android.content.Context;
import android.widget.Toast;

public class Utils
{
    /** Quick way to display a message */
    public static void showToast(Context context, String message)
    {
        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
    }
}
