package anass.lahlal.com.sosapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.ankit.gpslibrary.MyTracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {


int LOCATION_ACCESS;
String numbers = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.SEND_SMS};
        if (EasyPermissions.hasPermissions(this,perms))
        {
            return;
        }
        else
        {
            EasyPermissions.requestPermissions(this,"In order to access your location, you need to approve the permission !",LOCATION_ACCESS,perms);
        }



    }



    public void sosClicked(View view)
    {
        MyTracker position = new MyTracker(this);
        Geocoder geocoder;
        geocoder = new Geocoder(this,Locale.getDefault());
        List<Address> addresses;

        double longitude = position.getLongitude();
        double latitude = position.getLatitude();

        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        String message = sharedPreferences.getString("Sos","");
        numbers = sharedPreferences.getString("Phone","");
        String[] all = phoneNumbers(numbers);


        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);
            String address = addresses.get(0).getAddressLine(0);
            //String area = addresses.get(0).getLocality();
            String city = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();

            String fullAddress = address+", "+", "+city+", "+country+", "+postalCode;




            String fullMessage = message + "\n I'm here : \n"+fullAddress + "\n Longitude :\n"+ longitude + "\n Latitude :\n"+latitude;

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(numbers+"",null, fullMessage,null,null);











            Toast.makeText(this,"Your message has been sent",Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }








    }

    public void settings(View view) {
        Intent i = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(i);

    }

    public String[] phoneNumbers(String s)
    {

        String[] news = s.split("\\[");

        for (String a: news)
        {
            Log.d("regu",a);
        }

        return news;
    }
}
