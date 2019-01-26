package com.mahmoudbahaa.wallet.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.mahmoudbahaa.wallet.models.Account;
import com.mahmoudbahaa.wallet.models.AccountDao;
import com.mahmoudbahaa.wallet.models.Category;
import com.mahmoudbahaa.wallet.models.CategoryDao;
import com.mahmoudbahaa.wallet.models.Expense;
import com.mahmoudbahaa.wallet.models.ExpenseDao;
import com.mahmoudbahaa.wallet.models.Sequence;
import com.mahmoudbahaa.wallet.models.SequenceDao;
import com.mahmoudbahaa.wallet.models.Sync;
import com.mahmoudbahaa.wallet.models.SyncDao;


/**
 * Created by MahmoudBahaa on 13/01/2019.
 */

@Database(entities = {Expense.class, Account.class, Category.class, Sequence.class, Sync.class},version = 1,exportSchema = false)
@TypeConverters({DateConverter.class})

public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object Lock = new Object();
    private static final String DATABASE_NAME = "Expenses";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(final Context context){

        if (sInstance==null){
            synchronized (Lock){
                Log.d(LOG_TAG,"Creating New Database Instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,AppDatabase.DATABASE_NAME)

                     /*
                        addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);

                                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        getsInstance(context).accountDao().insertAll(Account.populateData());
                                        getsInstance(context).categoryDao().insertAll(Category.populateData());
                                    }

                                });

                            }
                        })
                        */
.allowMainThreadQueries()
                        .build();


            }

        }
        Log.v(LOG_TAG,"Getting Thr Database Instance");
        return sInstance;
    }


public abstract ExpenseDao expenseDao();

    public abstract AccountDao accountDao();

    public abstract CategoryDao categoryDao();

    public abstract SequenceDao sequenceDao();

       public abstract SyncDao syncDao();


}
