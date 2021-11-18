package com.example.assignment2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Global variables for all methods to use
    private List<Location> locationList;
    private Location toDelete;
    private Location toUpdate;

    //On create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Populating list from database on startup
        locationList = new ArrayList<>();
        //Doing all database interaction on different thread
        class getLocationTask extends AsyncTask<Void,Void,List<Location>> {
            protected List<Location> doInBackground(Void...voids){
                //Getting all locations
                return LocationDatabase.getLocationDatabase(getApplicationContext()).dao().getAllLocations();
            }

            //after database query add to global list
            protected void onPostExecute(List<Location> locations) {
                super.onPostExecute(locations);
                if (locations.size()<1)
                {
                    Toast.makeText(MainActivity.this,"The Database is Empty!",Toast.LENGTH_LONG).show();

                }
                else{
                    if(!locationList.isEmpty()){locationList.clear();}
                    locationList.addAll(locations);
                }
            }
        }
        //executing thread
        new getLocationTask().execute();

    }

    //View database button function
    public void viewDB(View view){
        class getLocationTask extends AsyncTask<Void,Void,List<Location>> {
            protected List<Location> doInBackground(Void...voids){
                //Getting all locations
                return LocationDatabase.getLocationDatabase(getApplicationContext()).dao().getAllLocations();
            }

            //After it gets the locations, loop through and make an alert with all info for each location
            protected void onPostExecute(List<Location> locations) {
                super.onPostExecute(locations);
                if (locations.size()<1)
                {
                    Toast.makeText(MainActivity.this,"The Database is Empty!",Toast.LENGTH_LONG).show();

                }
                else{
                    if(!locationList.isEmpty()){locationList.clear();}
                    locationList.addAll(locations);
                    StringBuffer buffer = new StringBuffer();

                    for(int i=0;i<locationList.size();i++){
                        buffer.append("ID: "+locationList.get(i).getId()+"\n");
                        buffer.append("Address: "+locationList.get(i).getAddress()+"\n");
                        buffer.append("Latitude: "+locationList.get(i).getLatitude()+"\n");
                        buffer.append("Longitude: "+locationList.get(i).getLongitude()+"\n");
                    }

                    AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);

                    build.setCancelable(true);
                    build.setTitle("Location Database");
                    build.setMessage(buffer.toString());
                    build.show();
                }
            }
        }
        //Execute the separate thread
        new getLocationTask().execute();

    }

    //Search function
    public void searchForLocation(View view){
        EditText searchField = (EditText) findViewById(R.id.inputSearch);

        //Checking if search field is empty
        if (searchField.getText().toString().isEmpty()){
            searchField.setError("Can not be empty!");
        }
        else{
            //Doing database interaction in a different thread
            class getLocationTask extends AsyncTask<Void,Void,List<Location>> {
                protected List<Location> doInBackground(Void...voids){
                    return LocationDatabase.getLocationDatabase(getApplicationContext()).dao().getAllLocations();
                }

                //After query, compare all addresses to search query and adding all results to an alert
                protected void onPostExecute(List<Location> locations) {
                    super.onPostExecute(locations);
                    if (locations.size()<1)
                    {
                        Toast.makeText(MainActivity.this,"The Database is Empty!",Toast.LENGTH_LONG).show();

                    }
                    else{
                        if(!locationList.isEmpty()){locationList.clear();}
                        locationList.addAll(locations);
                        String searchQuery = searchField.getText().toString().trim();
                        StringBuffer buffer = new StringBuffer();
                        boolean found =false;
                        for(int i=0;i<locationList.size();i++){
                            if(locationList.get(i).getAddress().toLowerCase().contains(searchQuery.toLowerCase()))
                            {
                                found=true;
                                buffer.append("ID: "+locationList.get(i).getId()+"\n");
                                buffer.append("Address: "+locationList.get(i).getAddress()+"\n");
                                buffer.append("Latitude: "+locationList.get(i).getLatitude()+"\n");
                                buffer.append("Longitude: "+locationList.get(i).getLongitude()+"\n");

                            }
                        }

                        if (!found){
                            Toast.makeText(getApplicationContext(), "No location found with that address!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);

                        build.setCancelable(true);
                        build.setTitle("Search Results");
                        build.setMessage(buffer.toString());
                        build.show();
                    }
                }
            }
            //Execute separate thread
            new getLocationTask().execute();

        }
    }
    //Function to switch activities to add a location
    public void addLocation(View view)
    {
        startActivity(new Intent( getApplicationContext(), addLocation.class));
    }

    //Update function
    public void updateLocation(View view){
        EditText idField = (EditText) findViewById(R.id.inputUpdateByID);

        //Check if id field empty
        if(idField.getText().toString().isEmpty())
        {
            Toast.makeText(this,"ID must not be empty!",Toast.LENGTH_SHORT).show();
        }
        else {
            //Do database interaction in seperate thread
            class getLocationTask extends AsyncTask<Void,Void,List<Location>> {
                protected List<Location> doInBackground(Void...voids){
                    return LocationDatabase.getLocationDatabase(getApplicationContext()).dao().getAllLocations();
                }

                //After database query, find the location with the specified id and change activities to update address within the database entry
                protected void onPostExecute(List<Location> locations) {
                    super.onPostExecute(locations);
                    if (locations.size()<1)
                    {
                        Toast.makeText(MainActivity.this,"The Database is Empty!",Toast.LENGTH_LONG).show();

                    }
                    else{
                        if(!locationList.isEmpty()){locationList.clear();}
                        locationList.addAll(locations);
                        boolean found =false;
                        for(int i=0;i<locationList.size();i++)
                        {
                            if(locationList.get(i).getId()==Integer.parseInt(idField.getText().toString()))
                            {
                                found=true;
                                toUpdate=locationList.get(i);
                                Intent intent = new Intent(getApplicationContext(), addLocation.class);
                                intent.putExtra("Update", true);
                                intent.putExtra("Location", toUpdate);
                                startActivity(intent);
                            }

                        }
                        if(!found){
                            Toast.makeText(getApplicationContext(),"No database entry found with that id",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            //execute separate thread
            new getLocationTask().execute();

        }
    }
    //Delete function
    public void deleteLocation(View view){
        EditText idField = (EditText) findViewById(R.id.inputUpdateByID);

        //Check if id field is empty
        if(idField.getText().toString().isEmpty())
        {
            Toast.makeText(this,"ID must not be empty!",Toast.LENGTH_LONG).show();
        }
        else{
            //Do all database interaction in seperate thread
            class getLocationTask extends AsyncTask<Void,Void,List<Location>> {
                protected List<Location> doInBackground(Void...voids){
                    return LocationDatabase.getLocationDatabase(getApplicationContext()).dao().getAllLocations();
                }

                //After database query, find entry to delete, and delete it
                protected void onPostExecute(List<Location> locations) {
                    super.onPostExecute(locations);
                    if (locations.size()<1)
                    {
                        Toast.makeText(MainActivity.this,"The Database is Empty!",Toast.LENGTH_LONG).show();

                    }
                    else{
                        if(!locationList.isEmpty()){locationList.clear();}
                        locationList.addAll(locations);
                        boolean found = false;
                        for(int i =0;i<locationList.size();i++){
                            if(locationList.get(i).getId()==Integer.parseInt(idField.getText().toString())){
                                found=true;
                                toDelete=locationList.get(i);
                                Toast.makeText(MainActivity.this,"Deleting "+toDelete.getAddress(),Toast.LENGTH_SHORT).show();
                                class deleteLocationTask extends AsyncTask<Void,Void,Void> {
                                    protected Void doInBackground(Void...voids){
                                        LocationDatabase.getLocationDatabase(getApplicationContext()).dao().deleteLocation(toDelete);
                                        return null;
                                    }

                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                    }
                                }
                                new deleteLocationTask().execute();
                                locationList.remove(i);
                            }
                            if(!found){
                                Toast.makeText(getApplicationContext(),"No database entry found with that id",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
            //Execute separate thread
            new getLocationTask().execute();

        }
    }

}