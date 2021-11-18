//Interface for the Database DAO

package com.example.assignment2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.assignment2.Location;

import java.util.List;
@Dao
public interface LocationDao {

    //Getting the entire database
    @Query("SELECT * FROM Locations")
    List<Location> getAllLocations();

    //Delete function
    @Delete
    void deleteLocation(Location location);

    //Add and Update function
    //The onConflictStrategy represents the update function
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addLocation(Location location);


}
