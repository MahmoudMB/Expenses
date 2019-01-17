package com.example.mahmoudbahaa.expenses.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by MahmoudBahaa on 16/01/2019.
 */


@Dao
public interface CategoryDao {

    @Query("SELECT * from Category")
    LiveData< List<Category>> loadAllCategories();


    @Query("SELECT * from Category where type = 'Income'")
    LiveData< List<Category>> loadIncomesCategories();


    @Query("SELECT * from Category where type = 'Outcome'")
    LiveData< List<Category>> loadOutcomesCategories();


    @Insert
    void insertCategory(Category category);


    @Delete
    void deleteCategory(Category category);


    @Query("SELECT * FROM Category WHERE id = :id")
    LiveData< Category> findCategory(int id);

}
