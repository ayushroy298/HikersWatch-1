package com.example.saumyaawasthi.hikerswatch;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    double latitude=0.0,longitude=0.0,accuracy=0.0,altitude=0.0;
    TextView textView;

   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);

        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location",location.toString());
                updateInfo(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location last=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(last!=null){
                updateInfo(last);
            }
        }
    }

    public void updateInfo(Location location){
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        accuracy=location.getAccuracy();
        altitude=location.getAltitude();
        String address="";

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);
            if(addressList!=null && addressList.size()>0){
                Log.i("Address",addressList.get(0).toString());
                if(addressList.get(0).getAdminArea()!=null){
                    address+=addressList.get(0).getAdminArea()+" ";
                }
                if(addressList.get(0).getPostalCode()!=null){
                    address+="-"+addressList.get(0).getPostalCode()+", ";
                }
                if(addressList.get(0).getCountryName()!=null){
                    address+=addressList.get(0).getCountryName();
                }
            }
            else{
                address="Sorry unable to find the address :(";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        textView.setText("Latitude : " + latitude + "\nLongitude : " + longitude + "\nAccuracy : " + accuracy + "\nAltitude : " + altitude + "\nAddress : " + address);
    }
}
