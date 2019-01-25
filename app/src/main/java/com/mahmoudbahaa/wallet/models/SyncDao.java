package com.mahmoudbahaa.expenses.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Created by MahmoudBahaa on 21/01/2019.
 */


@Dao
public interface SyncDao {

    @Query("DELETE FROM Sync")
    void DeleteTable();



    @Query("SELECT * from Sync ")
    Sync  loadSyncData();


    @Query("SELECT * from Sync ")
   LiveData <Sync>  loadSyncDataLive();

    @Insert
    void insertSync(Sync sync);




    @Update(onConflict = OnConflictStrategy.REPLACE)
    void UpdateSync(Sync sync);
}
