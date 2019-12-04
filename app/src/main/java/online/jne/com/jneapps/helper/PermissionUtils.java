package online.jne.com.jneapps.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


public class PermissionUtils {

    private Context context;

    public PermissionUtils(Context context) {
        this.context = context;
    }


    public boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(Activity activity){
        int PERMISSION_REQUEST_CODE = 555;
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
        }, PERMISSION_REQUEST_CODE);
    }

    public boolean isLocationServiceEnabled() {
        LocationManager locationManager;
        boolean gps_enabled = false, network_enabled = false;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            showLog("gps =" + gps_enabled);
        } catch (Exception ignored) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            showLog("network =" + network_enabled);
        } catch (Exception ignored) {
        }

        return gps_enabled || network_enabled;

    }

    private void showLog(String text){
        Log.d("tes",text);
    }

}
