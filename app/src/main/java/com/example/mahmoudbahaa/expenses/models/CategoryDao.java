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
public interface CategoryDao {

    @Query("SELECT * from Category")
    List<Category> loadAllCategories();


    @Insert
    void insertCategory(Category category);


    @Delete
    void deleteCategory(Category category);


    @Query("SELECT * FROM Category WHERE id = :id")
    Category findCategory(int id);

}
