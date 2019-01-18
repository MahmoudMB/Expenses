package com.example.mahmoudbahaa.expenses.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by MahmoudBahaa on 16/01/2019.
 */



@Dao
public interface AccountDao {

    @Query("SELECT * from Account")
    LiveData< List<Account> >loadAllAccounts();


    @Insert
    void insertAccount(Account account);


    @Delete
    void deleteAccount(Account account);

    @Query("SELECT * FROM Account WHERE id = :id")
    LiveData<   Account > findAccount(int id);

    @Insert
    void insertAll(Account... dataEntities);

    @Query("SELECT * FROM Account WHERE defaultAccount  = :value")
    LiveData< Account> LoadDefaultAccount(boolean value);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void UpdateAccount(Account account);


}
