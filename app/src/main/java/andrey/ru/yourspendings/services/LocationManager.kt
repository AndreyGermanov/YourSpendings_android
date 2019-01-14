package andrey.ru.yourspendings.services
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
@SuppressLint("MissingPermission")

/**
 * Created by Andrey Germanov on 1/6/19.
 */
object LocationManager {
    private const val LOCATION_REQUEST_CODE = 1
    private const val CAMERA_REQUEST_CODE = 1
    @SuppressLint("StaticFieldLeak")
    lateinit var client: FusedLocationProviderClient

    fun getLocation(callback:(latitude:Double, longitude:Double)->Unit) {
        client.lastLocation
            .addOnSuccessListener { callback(it?.latitude ?: 0.0,it?.longitude ?: 0.0) }
            .addOnFailureListener { callback(0.0,0.0) }
    }

    fun setup(context:Activity) {
        client = LocationServices.getFusedLocationProviderClient(context)
        if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),LOCATION_REQUEST_CODE)
        }
        if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),LOCATION_REQUEST_CODE)
        }
        if (ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,arrayOf(Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
        }
    }
}