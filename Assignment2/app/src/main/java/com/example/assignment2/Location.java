//Class for Location Object

package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Defining this class as the entity for Locations table in Room database
@Entity(tableName = "Locations")
public class Location implements Serializable {
    //Primary key ID
    @PrimaryKey(autoGenerate = true)
    private int id;

    //All columns
    @ColumnInfo(name = "Address")
    private String address;

    @ColumnInfo(name = "Latitude")
    private float latitude;

    @ColumnInfo(name = "Longitude")
    private float longitude;


    //Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) { this.address = address; }

    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) { this.latitude = latitude; }

    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) { this.longitude = longitude; }
}
