package com.example.mahmoudbahaa.expenses.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mahmoudbahaa.expenses.data.AppDatabase;

import java.util.List;

/**
 * Created by MahmoudBahaa on 16/01/2019.
 */

public class MainViewModel extends AndroidViewModel {


    private LiveData<List<Expense>> expenses;

   private Long start;
   private Long end;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        Log.v("start in constr",start+"");
        Log.v("ed in constr",end+"");
        expenses = database.expenseDao().loadAllExpenses(start,end);
    }


    public LiveData<List<Expense>> getExpenses(Long start,Long end) {
        this.start = start;
        this.end = end;
        Log.v("start in ",start+"");
        Log.v("ed in ",end+"");
        return expenses;
    }
}
