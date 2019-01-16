package com.example.mahmoudbahaa.expenses.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by MahmoudBahaa on 16/01/2019.
 */



@Dao
public interface AccountDao {

    @Query("SELECT * from Account")
    List<Account> loadAllAccounts();


    @Insert
    void insertAccount(Account account);


    @Delete
    void deleteAccount(Account account);

    @Query("SELECT * FROM Account WHERE id = :id")
    Account findAccount(int id);
}
