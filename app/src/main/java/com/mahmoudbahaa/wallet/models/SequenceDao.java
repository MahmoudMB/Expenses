package com.mahmoudbahaa.wallet.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by MahmoudBahaa on 21/01/2019.
 */

@Dao
public interface SequenceDao {


    @Query("DELETE FROM Sequence")
    void DeleteTable();


    @Query("SELECT * from Sequence where name = 'Account' ")
    Sequence  loadAccountSeq();

    @Query("SELECT * from Sequence where name = 'Category' ")
    Sequence  loadCategorySeq();

    @Query("SELECT * from Sequence where name = 'Expense' ")
    Sequence  loadExpenseSeq();


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void UpdateAccountSeq(Sequence account);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void UpdateCategorySeq(Sequence  category);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void UpdateExpenseSeq(Sequence expense);


    @Insert
    void insertAll(Sequence... dataEntities);


    @Query("SELECT * from Sequence")
    List<Sequence> loadAllSequenceAllList();

}
