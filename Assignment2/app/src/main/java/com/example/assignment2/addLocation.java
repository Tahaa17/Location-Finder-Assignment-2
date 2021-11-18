//Add Location Activity

package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class addLocation extends AppCompatActivity {

    //Variables for input field and location to update if update was called
    private EditText inputAddress;
    private Location readyToUpdateLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        inputAddress = (EditText) findViewById(R.id.addressField);
        TextView header = (TextView) findViewById(R.id.headerText);
        header.setText("Add Location");

        //Checking if this is an update execution
        if(getIntent().getBooleanExtra("Update",false)){
            readyToUpdateLocation = (Location) getIntent().getSerializableExtra("Location");
            setUpdateNote();
        }
    }
    //if updating populate fields with previous data
    public void setUpdateNote(){
        TextView header = (TextView) findViewById(R.id.headerText);
        header.setText("Update Location "+readyToUpdateLocation.getId());
        inputAddress.setText(readyToUpdateLocation.getAddress());
    }
    //Save to database function
    public void saveToDB(View view){

            //Getting string of address
            final String addressString = inputAddress.getText().toString().trim();
            //Getting latitude/longitude using geocoder object
            Geocoder geocoder = new Geocoder(addLocation.this);
            List<Address> list = new ArrayList<>();

            try{
                list = geocoder.getFromLocationName(addressString, 1);
            }catch (IOException e){
                e.printStackTrace();
            }
            //Creating location object to push to database
            final Location newLocation = new Location();
            if(list.size() > 0){
                Address address = list.get(0);
                final float latitude = (float)address.getLatitude();
                final float longitude = (float)address.getLongitude();
                newLocation.setAddress(addressString);
                newLocation.setLatitude(latitude);
                newLocation.setLongitude(longitude);
                //If this is an update execution, set the id to the previous entries id so that it is replaced
                if(readyToUpdateLocation!=null){
                    newLocation.setId(readyToUpdateLocation.getId());
                }
                Toast.makeText(addLocation.this,"Found New Address",Toast.LENGTH_SHORT).show();
            }

            //Separating all database interaction to a different thread
            @SuppressLint("StaticFieldLeak")
            class saveLocationTask extends AsyncTask<Void, Void, Void> {
                @Override
                //Upload to database
                protected Void doInBackground(Void... voids) {
                    LocationDatabase.getLocationDatabase(getApplicationContext()).dao().addLocation(newLocation);
                    return null;
                }

                //After uploaded go back to main page
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Toast.makeText(addLocation.this,"Saved to DB",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
            //Execute the seperate thread
            new saveLocationTask().execute();


    }
}