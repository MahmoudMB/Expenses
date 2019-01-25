package com.mahmoudbahaa.expenses.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;



/**
 * Created by MahmoudBahaa on 13/01/2019.
 */

@Dao
public interface ExpenseDao {


    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to ORDER BY createdAt DESC")
    LiveData< List<Expense>> loadAllExpenses(Long from, Long to);

    @Query("DELETE FROM Expense")
    void DeleteTable();

    @Query("DELETE FROM Expense where accountId = :id ")
    void DeleteAllExpensesWithAccountId(int id);

    @Query("DELETE FROM Expense where categoryId = :id ")
    void DeleteAllExpensesWithCategoryId(int id);


    @Query("SELECT * from Expense")
    LiveData< List<Expense>> loadAllExpensesAll();


    @Query("SELECT * from Expense")
       List<Expense> loadAllExpensesAllList();

    @Insert
    void insertExpense(Expense expense);

    @Insert
    void insertAll(Expense... dataEntities);

    @Delete
    void deleteExpense(Expense expense);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void UpdateExpense(Expense expense);



    @Query("SELECT * FROM EXPENSE WHERE id = :id")
    LiveData<Expense> loadExpeseById(int id);


    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to And accountId = :id ORDER BY createdAt DESC")
    LiveData< List<Expense>> loadExpeseByAccountId(Long from, Long to,int id);

    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to And type = :type ORDER BY createdAt DESC")
    LiveData< List<Expense>> loadExpeseByType(Long from, Long to,String type);




    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to And  categoryId =:catId ORDER BY createdAt DESC")
    LiveData< List<Expense>> loadExpese_c(Long from, Long to,int catId);








    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to And  accountId = :aId  ORDER BY createdAt DESC")
    LiveData< List<Expense>> loadExpese_a(Long from, Long to,int aId);








    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to And  accountId = :aId And categoryId =:catId ORDER BY createdAt DESC")
    LiveData< List<Expense>> loadExpese_a_c(Long from, Long to,int aId,int catId);









    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to And type = :type  ORDER BY createdAt DESC")
    LiveData< List<Expense>> loadExpese_t(Long from, Long to,String type);





    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to And type = :type  And categoryId =:catId ORDER BY createdAt DESC")
    LiveData< List<Expense>> loadExpese_t_c(Long from, Long to,String type,int catId);

    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to And type = :type  And accountId = :aId ORDER BY createdAt DESC")
    LiveData< List<Expense>> loadExpese_t_a(Long from, Long to,String type,int aId);


    @Query("SELECT * from Expense Where createdAt BETWEEN :from AND :to And type = :type  And accountId = :aId And categoryId =:catId ORDER BY createdAt DESC")
    LiveData< List<Expense>> loadExpese_t_a_c(Long from, Long to,String type,int aId,int catId);





}
