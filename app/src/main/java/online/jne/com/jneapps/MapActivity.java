package online.jne.com.jneapps;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import mehdi.sakout.fancybuttons.FancyButton;
import online.jne.com.jneapps.connection.ApiClient;
import online.jne.com.jneapps.helper.PathJSONParser;
import online.jne.com.jneapps.helper.PermissionUtils;
import online.jne.com.jneapps.helper.SessionManager;
import online.jne.com.jneapps.helper.Utils;
import online.jne.com.jneapps.model.Cabang;
import online.jne.com.jneapps.model.History;
import online.jne.com.jneapps.model.OrderResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String LOG_TAG = "TAG";
    private static GoogleMap mMap;
    FrameLayout layout;
    Location mLastLocation;
    PermissionUtils permissionUtils;
    Toolbar toolbar;
    FancyButton btnMap;
    LatLng latLng;
    Cabang cabang;
    String kete;
    String berat;
    LatLng lokasiUser;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        permissionUtils = new PermissionUtils(this);
        btnMap = findViewById(R.id.btnMapp);
        layout    = findViewById(R.id.Flayout);


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lokasi anda dan JNE");

        Intent x = getIntent();

        cabang = new Cabang();
        lokasiUser = new LatLng(x.getDoubleExtra("lat",0.0),x.getDoubleExtra("long",0.0));

        Log.d("tes",getAddressFromLocation(lokasiUser.latitude,lokasiUser.longitude));

        cabang = x.getParcelableExtra("cabang");
        kete = x.getStringExtra("ket");
        berat = x.getStringExtra("berat");

        latLng = new LatLng(Double.parseDouble(cabang.getLat()),Double.parseDouble(cabang.getLng()));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SessionManager sessionManager = new SessionManager(MapActivity.this);
                postOrder(sessionManager.getId(),
                        String.valueOf(lokasiUser.latitude),
                        String.valueOf(lokasiUser.longitude),
                        cabang.getId_cabang(),
                        berat,
                        kete);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiUser, 10f));
        String url = getMapsApiDirectionsUrl();
        //String url = "http://maps.googleapis.com/maps/api/directions/json?origin=Adelaide,SA&destination=Adelaide,SA&waypoints=optimize:true|Barossa+Valley,SA|Clare,SA|Connawarra,SA|McLaren+Vale,SA&sensor=false";
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title(cabang.getNama()));
        Marker marker2 = mMap.addMarker(new MarkerOptions().position(lokasiUser)
                .title("lokasi kamu"));

        builder.include(marker.getPosition());
        builder.include(marker2.getPosition());
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (height * 0.15); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        mMap.animateCamera(cu);
        marker.showInfoWindow();
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                URL urlx = new URL(url[0]);
                data = getResponseFromHttpUrl(urlx);
                JSONObject response = new JSONObject(data);
                JSONArray routes = response.getJSONArray("routes");
                JSONObject waypoint = routes.getJSONObject(0);
                final JSONArray legs = waypoint.getJSONArray("legs");
                final JSONArray order = waypoint.getJSONArray("waypoint_order");
                Handler handler = new Handler(Looper.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < order.length(); i++) {
                            try {
                                JSONObject leg = legs.getJSONObject(i);
                                JSONObject distance = leg.getJSONObject("distance");
                                JSONObject duration = leg.getJSONObject("duration");
                                String jarak = distance.getString("text");
                                Log.d("tes","jarak : " + jarak);
                                String waktu = splitToComponentTimes(duration.getString("value"));
                                Log.d("tes","waktu : " + waktu);
                                Log.d("tes", i + " : " + order.get(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

                Log.d("tes", "hasil : " + order.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        public String splitToComponentTimes(String biggy)
        {

            long longVal = Long.parseLong(biggy);
            int hours = (int) longVal / 3600;
            int remainder = (int) longVal - hours * 3600;
            int mins = remainder / 60;
            return hours +" Jam " + mins + " Menit";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private String getMapsApiDirectionsUrl() {

        String origindest = "origin=" + lokasiUser.latitude + "," + lokasiUser.longitude + "&destination=" + latLng.latitude + "," + latLng.longitude;
        String params = origindest;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params +"&key=AIzaSyDbpwWoN0-w-nTPR_71loLdxwrpld8hxBg";
        Log.d(LOG_TAG, url);
        return url;
    }
    private String getAddressFromLocation(double latitude, double longitude) {

        String addressText="";
        Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
        if (Geocoder.isPresent()) {
            try {
                List<Address> list = geocoder.getFromLocation(latitude, longitude, 1);
                Address address = (list.isEmpty() ? null : list.get(0));
                String subadmin = address.getSubAdminArea();
                String country = address.getCountryName();
                addressText = String.format("%s, %s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getLocality(), address.getCountryName());

            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {

        }
        return addressText;
    }


    private class ParserTask extends
            AsyncTask<String, Integer, PolylineOptions> {

        @Override
        protected PolylineOptions doInBackground(
                String... jsonData) {

            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);

                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(5);
                    polyLineOptions.color(Color.BLUE);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return polyLineOptions;
        }

        @Override
        protected void onPostExecute(PolylineOptions routes) {

            if (routes != null) {
                Polyline polyline = mMap.addPolyline(routes);
                layout.setAlpha(1);
            }
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(10000);
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private void postOrder(String id_costumer,String lat,String lng,String id_cabang,String berat,String keterangan){
        ApiClient.getTiketApiClient().postOrder(id_costumer,lat,lng,id_cabang,berat,keterangan).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if(response.body()!=null) {
                    Intent x = new Intent(getApplicationContext(), PulseActiviy.class);
                    History history = new History();
                    history.setKodeOrder(response.body().getKodeOrder());
                    history.setUploadTime(Utils.convertDatetoString(Calendar.getInstance().getTime()));
                    history.setCabang(cabang.getNama());
                    history.setKisaranBerat(berat);
                    history.setStatus("0");
                    x.setFlags(x.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    x.putExtra("order", history);
                    x.putExtra("selesai", true);
                    startActivity(x);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
