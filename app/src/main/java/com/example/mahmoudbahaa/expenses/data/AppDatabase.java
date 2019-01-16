package com.example.mahmoudbahaa.expenses.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.mahmoudbahaa.expenses.models.Account;
import com.example.mahmoudbahaa.expenses.models.AccountDao;
import com.example.mahmoudbahaa.expenses.models.Category;
import com.example.mahmoudbahaa.expenses.models.CategoryDao;
import com.example.mahmoudbahaa.expenses.models.Expense;
import com.example.mahmoudbahaa.expenses.models.ExpenseDao;


/**
 * Created by MahmoudBahaa on 13/01/2019.
 */

@Database(entities = {Expense.class, Account.class, Category.class},version = 1,exportSchema = false)
@TypeConverters({DateConverter.class})

public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object Lock = new Object();
    private static final String DATABASE_NAME = "Expenses";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context){

        if (sInstance==null){
            synchronized (Lock){
                Log.d(LOG_TAG,"Creating New Database Instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,AppDatabase.DATABASE_NAME)
                        .build();


            }

        }
        Log.v(LOG_TAG,"Getting Thr Database Instance");
        return sInstance;
    }


public abstract ExpenseDao expenseDao();

    public abstract AccountDao accountDao();

    public abstract CategoryDao categoryDao();




}
