package online.jne.com.jneapps

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.smarteist.autoimageslider.SliderLayout
import com.smarteist.autoimageslider.SliderView
import online.jne.com.jneapps.helper.PermissionUtils
import online.jne.com.jneapps.helper.SessionManager


@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {

    private lateinit var sliderLayout: SliderLayout
    private val code = 555
    private lateinit var mLocationManager: LocationManager
    private lateinit var permissionUtils: PermissionUtils
    lateinit var userLocation: Location
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(myToolbar)

        userLocation = Location("")
        permissionUtils = PermissionUtils(this@MainActivity)
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (permissionUtils.checkPermissions()) {
            if (permissionUtils.isLocationServiceEnabled) {
                val location = getLastBestLocation()
                if (location != null) {
                    userLocation = location;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                        1f, mLocationListener)
            }
            else{
                askUserToEnableLocation()
            }
        }else{
            permissionUtils.requestPermission(this@MainActivity)
        }


        supportActionBar!!.title = "   JNE Jemput"
        myToolbar.setTitleTextColor(resources.getColor(R.color.white))

        val map = findViewById<CardView>(R.id.map)
        val track = findViewById<CardView>(R.id.track)
        val tarif = findViewById<CardView>(R.id.tarif)
        val help = findViewById<CardView>(R.id.help)
        val about = findViewById<CardView>(R.id.about)
        val history = findViewById<CardView>(R.id.history)
        val text = findViewById<TextView>(R.id.txtName)

        sessionManager = SessionManager(this@MainActivity)

        text.text = "Halo selamat datang," + sessionManager.data.name

        sliderLayout = findViewById(R.id.imageSlider)
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL) //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.scrollTimeInSec = 2 //set scroll delay in seconds :

        setSliderViews()


        map.setOnClickListener {
            if (permissionUtils.checkPermissions()) {
                if (permissionUtils.isLocationServiceEnabled) {
                    if (userLocation.latitude != 0.0 && userLocation.longitude != 0.0) {
                        val x = Intent(this@MainActivity, OrderActivity::class.java)
                        x.putExtra("lat", userLocation.latitude)
                        x.putExtra("long", userLocation.longitude)
                        startActivity(x)
                    } else {
                        Toast.makeText(this, "Mohon tunggu update lokasi sebelum memesan", Toast.LENGTH_SHORT).show()
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                                1f, mLocationListener)
                    }
                } else {
                   askUserToEnableLocation()
                }
            }
            else{
                askUserToAllowPermission()
            }

        }
        track.setOnClickListener {
            val x = Intent(this@MainActivity, WebActivity::class.java)
            x.putExtra("url", "https://www.jne.co.id/id/tracking/tarif")
            startActivity(x)
        }
        tarif.setOnClickListener {
            val x = Intent(this@MainActivity, WebActivity::class.java)
            x.putExtra("url", "https://www.jne.co.id/id/tracking/trace")
            startActivity(x)
        }
        help.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=6282272824752"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        history.setOnClickListener {
            val x = Intent(this@MainActivity, HIstoryActivity::class.java)
            startActivity(x)
        }
        about.setOnClickListener {
            val x = Intent(this@MainActivity, AboutActivity::class.java)
            startActivity(x)
        }

    }


    private fun setSliderViews() {

        for (i in 0..3) {

            val sliderView = SliderView(this)

            when (i) {
                0 -> sliderView.imageUrl = "https://www.jne.co.id/contents/slider-217.jpg?1542042589220"
                1 -> sliderView.imageUrl = "https://www.jne.co.id/contents/slider-236.jpg?1542042706724"
                2 -> sliderView.imageUrl = "https://www.jne.co.id/contents/slider-241.jpg?1542042615296"
                3 -> sliderView.imageUrl = "https://www.jne.co.id/contents/slider-242.jpg?1542042606324"
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            sliderView.setOnSliderClickListener {
                var url = "http://www.jne.com"
                when(i){
                    0 -> url = "https://www.jne.co.id/id/produk-dan-layanan/jne-logistic"
                    1 -> url = "https://jlc.jne.co.id/4"
                    2 -> url = "https://jlc.jne.co.id/"
                    3 -> url = "https://www.jne.co.id/id/produk-dan-layanan/jne-express/money-remittance"
                }

                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_logout) {
            val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(this@MainActivity, android.R.style.Theme_Material_Dialog_Alert)
            } else {
                AlertDialog.Builder(this@MainActivity)
            }
            builder.setTitle("Logout")
                    .setMessage("Anda yakin ingin logout?")
                    .setPositiveButton("Ya") { dialog, which ->

                        val sessionManager = SessionManager(this@MainActivity)
                        sessionManager.setLogin(false);
                        val x = Intent(this@MainActivity, LoginActivity::class.java)
                        x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(x)
                    }
                    .setNegativeButton("Tidak") { dialog, which -> dialog.cancel() }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()

        }

        return super.onOptionsItemSelected(item)
    }

    private fun getLastBestLocation(): Location? {

        val locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        var GPSLocationTime: Long = 0
        if (null != locationGPS) {
            GPSLocationTime = locationGPS.time
        }

        var NetLocationTime: Long = 0

        if (null != locationNet) {
            NetLocationTime = locationNet.time
        }

        if (0 < GPSLocationTime - NetLocationTime) {
            return locationGPS
        } else {
            return locationNet
        }
    }

    private fun askUserToEnableLocation() {
        val builder: android.app.AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = android.app.AlertDialog.Builder(this@MainActivity, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        } else {
            builder = android.app.AlertDialog.Builder(this@MainActivity)
        }
        builder.setMessage("Kami memerlukan lokasi kamu untuk menentukan lokasi. Mohon menghidupkan pengaturan lokasi kamu dengan memilih pengaturan berikut.")
                .setPositiveButton("Pengaturan Lokasi") { dialog, which ->
                    val callGPSSettingIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(callGPSSettingIntent, code)
                }
                .setNegativeButton("Keluar") { dialog, which -> this.finish() }
                .show()
    }

    private fun askUserToAllowPermission() {
        val builder: android.app.AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = android.app.AlertDialog.Builder(this@MainActivity, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        } else {
            builder = android.app.AlertDialog.Builder(this@MainActivity)
        }
        builder.setMessage("Kami memerlukan lokasi kamu untuk menentukan lokasi. Mohon memberi izin untuk melanjutkan.")
                .setPositiveButton("Ok") { dialog, which -> permissionUtils.requestPermission(this@MainActivity) }
                .show()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == code) {
            if (grantResults.isEmpty()) {
                askUserToAllowPermission()
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userLocation = Location("")
                val location = getLastBestLocation()
                if (location != null) {
                    userLocation = location
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                            1f, mLocationListener)
                }
            } else {
                askUserToAllowPermission()
            }
        }
    }

    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            userLocation = location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

}
