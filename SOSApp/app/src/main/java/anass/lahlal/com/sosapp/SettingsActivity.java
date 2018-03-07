package anass.lahlal.com.sosapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingsActivity extends AppCompatActivity {

    int CONTACT_PICKER_REQUEST ;
    EditText et, etm;
    boolean check = false;
    String allNumbers = "";
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        et = findViewById(R.id.etContact);
        etm = findViewById(R.id.etMessage);


            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            checkSharedPreferences(sharedPreferences);



    }


    public void chooseContact(View view)
    {

        et.setText("");
        check = false;
        String[] perms = {Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(this,perms))
        {
            new MultiContactPicker.Builder(SettingsActivity.this)
                    .hideScrollbar(false)
                    .showTrack(true) //Optional - default: true
                    .searchIconColor(Color.WHITE) //Option - default: Whit
                    .bubbleTextColor(Color.WHITE) //Optional - default: White
                    .showPickerForResult(CONTACT_PICKER_REQUEST);
        }

        else
            {
                EasyPermissions.requestPermissions(this,"In order to choose a contact, you need to approve the permission !",CONTACT_PICKER_REQUEST,perms);
            }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {

                int i = 0;
                List<ContactResult> results = MultiContactPicker.obtainResult(data);

                for (ContactResult c: results) {
                    Log.d("MyTag", results.get(i).getDisplayName() + " "+results.get(i).getPhoneNumbers());

                    et.append(results.get(i).getDisplayName().toString()+" "+results.get(i).getPhoneNumbers()+ " , ");
                    allNumbers+=results.get(i).getPhoneNumbers().toString();

                    i++;
                    count = i;
                }

                check = true;



            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }

    public void savePreferences(View view)
    {
        SharedPreferences shared = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        String s = etm.getText().toString();

        editor.putString("Contacts",et.getText().toString());
        editor.putString("Sos",s);
        editor.putString("Phone",allNumbers);
        editor.putInt("Counter", count);

        editor.putBoolean("Added",check);
        editor.commit();


        Toast.makeText(this,"Saved !",Toast.LENGTH_LONG).show();

        //Intent i = new Intent(this,MainActivity.class);
        //startActivity(i);
        finish();

    }

    public void checkSharedPreferences(SharedPreferences sp)
    {
        if(sp.getBoolean("Added",true))
        {
            check = sp.getBoolean("Added",true);
            et.setText(sp.getString("Contacts",""));
            etm.setText(sp.getString("Sos",""));
        }

        Log.d("This",sp.getString("Contacts",""));
    }
}
