package com.example.mahmoudbahaa.expenses.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;



/**
 * Created by MahmoudBahaa on 13/01/2019.
 */

@Dao
public interface ExpenseDao {


    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to ORDER BY createdAt DESC")
   LiveData< List<Expense>> loadAllExpenses(Long from, Long to);



    @Insert
    void insertExpense(Expense expense);


    @Delete
    void deleteExpense(Expense expense);

}
