package com.example.mahmoudbahaa.expenses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Expense;

import java.util.List;

/**
 * Created by MahmoudBahaa on 16/01/2019.
 */

public class LoadExpensesViewModel extends ViewModel{

    public LiveData<List<Expense>> getExpenses() {
        return expenses;
    }

    public LoadExpensesViewModel(AppDatabase database,long start,long end) {
        Log.v("startincon2",start+"");
        this.expenses = database.expenseDao().loadAllExpenses(start,end);
    }




    private LiveData<List<Expense>> expenses;




}
