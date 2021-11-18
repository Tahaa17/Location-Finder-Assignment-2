//Class representing the database

package com.example.assignment2;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Location.class, version = 1, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase {

    private static LocationDatabase locationDatabase;

    //Creating database if not already created
    public static synchronized LocationDatabase getLocationDatabase(Context context) {
        if (locationDatabase == null) {
            locationDatabase = Room.databaseBuilder(
                    context,
                    LocationDatabase.class,
                    "location_db"
            ).build();
        }
        //Returning database
        return locationDatabase;
    }

    public abstract LocationDao dao();
}

