package com.example.mahmoudbahaa.expenses;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.mahmoudbahaa.expenses.data.AppDatabase;

/**
 * Created by MahmoudBahaa on 16/01/2019.
 */

public class LoadExpensesViewModelFactory extends ViewModelProvider.NewInstanceFactory {


private final AppDatabase mDb;

private final long start;
private final long end;

    public LoadExpensesViewModelFactory(AppDatabase mDb, long start, long end) {
        this.mDb = mDb;
        this.start = start;
        this.end = end;

    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

return (T) new LoadExpensesViewModel(mDb,start,end);
    }
}
