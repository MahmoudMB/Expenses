package com.example.mahmoudbahaa.expenses;

import android.app.IntentService;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Account;
import com.example.mahmoudbahaa.expenses.models.Category;
import com.example.mahmoudbahaa.expenses.models.Expense;
import com.example.mahmoudbahaa.expenses.models.Sequence;
import com.example.mahmoudbahaa.expenses.models.Sync;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MahmoudBahaa on 21/01/2019.
 */

public class SyncDataService extends IntentService {

    public SyncDataService() {
        super("SyncDataService");
    }
    ResultReceiver rec;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


       rec= intent.getParcelableExtra("receiverTag");

        SyncFromDatabase();

    }

    String GetUserId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Id = preferences.getString("Id", "");


        return Id;

    }

    void SyncFromDatabase()
    {


        AppDatabase.getsInstance(this).categoryDao().DeleteTable();
        AppDatabase.getsInstance(this).accountDao().DeleteTable();
        AppDatabase.getsInstance(this).expenseDao().DeleteTable();
        AppDatabase.getsInstance(this).sequenceDao().DeleteTable();
        AppDatabase.getsInstance(this).syncDao().DeleteTable();


        String userId = GetUserId();

          storageRef = storage.getReference().child("Users").child(userId).child("images");



        ValueEventListener AccountListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                List<Account> accounts = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    Account account = new Account(postSnapshot);
                    accounts.add(account);
                }

                final Account[] arrayAccount = accounts.toArray(new Account[accounts.size()]);

                AppDatabase.getsInstance(SyncDataService.this).accountDao().insertAll(arrayAccount);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Tag", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Accounts").addListenerForSingleValueEvent(AccountListener);




        ValueEventListener CategoriesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                List<Category> categories = new ArrayList<>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    Category category = new Category(postSnapshot);
                    categories.add(category);

                }

                final Category[] arrayCategory = categories.toArray(new Category[categories.size()]);



                AppDatabase.getsInstance(SyncDataService.this).categoryDao().insertAll(arrayCategory);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Tag", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Categories").addListenerForSingleValueEvent(CategoriesListener);







        ValueEventListener SequenceListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                List<Sequence> sequences = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    Sequence sequence = new Sequence(postSnapshot);
                    sequences.add(sequence);

                }

                final Sequence[] arraysequences = sequences.toArray(new Sequence[sequences.size()]);
                AppDatabase.getsInstance(SyncDataService.this).sequenceDao().insertAll(arraysequences);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Tag", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Sequence").addListenerForSingleValueEvent(SequenceListener);





        ValueEventListener SyncListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI


                    Sync sync = new Sync(dataSnapshot);


                AppDatabase.getsInstance(SyncDataService.this).syncDao().insertSync(sync);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Tag", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Sync").addListenerForSingleValueEvent(SyncListener);




        ValueEventListener expenseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                List<Expense> expenses = new ArrayList<>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    Expense expense = new Expense(postSnapshot);
                    expenses.add(expense);
                    if (!TextUtils.isEmpty( expense.getImagePath()))
                    DownloadImages(expense.getImagePath());
                }

                final Expense[] array = expenses.toArray(new Expense[expenses.size()]);
                AppDatabase.getsInstance(SyncDataService.this).expenseDao().insertAll(array);


                Bundle b=new Bundle();
                rec.send(0, b);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Tag", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Expenses").addListenerForSingleValueEvent(expenseListener);











    }


    void DownloadImages(String path) {

            final String imageName = path.substring(path.lastIndexOf("/") + 1);

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File mypath = new File(directory, imageName);

            Log.v("Mypath", mypath.exists() + "");

               if (!mypath.exists())
               {
                   storageRef.child(imageName).getFile(mypath).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created

                    Log.v("File" + imageName, "Synced");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

               }

    }
}
