package com.mahmoudbahaa.wallet.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.mahmoudbahaa.wallet.data.AppDatabase;
import com.mahmoudbahaa.wallet.models.Account;
import com.mahmoudbahaa.wallet.models.Category;
import com.mahmoudbahaa.wallet.models.Expense;
import com.mahmoudbahaa.wallet.models.Sequence;
import com.mahmoudbahaa.wallet.models.Sync;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MahmoudBahaa on 20/01/2019.
 */

public class BackUpService extends IntentService {
    Boolean status = false;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef;

    public BackUpService() {
        super("BackUpService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


      String action =   intent.getAction();
      if (action.equals("SignOut"))
         status = true;

        CreateDbCopy(intent.getStringExtra("UserId"));
    }

    void CreateDbCopy(String userID){

        final  String userId = userID;
         storageRef = storage.getReference().child("Users").child(userId).child("images");




               final List<Expense> expenses = AppDatabase.getsInstance(this).expenseDao().loadAllExpensesAllList();

                Map map;
                for (int i=0;i<expenses.size();i++)
                {

                    final Expense expense = expenses.get(i);
                    map=new HashMap<>();
                    map.put("id",expenses.get(i).getId());
                    map.put("description",expenses.get(i).getDescription());
                  //  map.put("category",expenses.get(i).getCategory());
                  //  map.put("account",expenses.get(i).getAccount());
                    map.put("type",expenses.get(i).getType());
                    map.put("price",expenses.get(i).getPrice());
                    map.put("memo",expenses.get(i).getMemo());
                    map.put("imagePath",expenses.get(i).getImagePath());
                    map.put("createdAt",expenses.get(i).getCreatedAt().getTime());
                    map.put("accountId",expenses.get(i).getAccountId());
                    map.put("categoryId",expenses.get(i).getCategoryId());

                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Expenses").child(String.valueOf(expenses.get(i).getId())).setValue(map);

if (!TextUtils.isEmpty(expenses.get(i).getImagePath())){


   final String imageName = expenses.get(i).getImagePath().substring(expenses.get(i).getImagePath().lastIndexOf("/")+1);

    storageRef.child(imageName).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
        @Override
        public void onSuccess(StorageMetadata storageMetadata) {

        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

            try {

                InputStream stream = new FileInputStream(new File(expense.getImagePath()));

                Log.v("ImagePath",expense.getImagePath());
                Log.v("imageName",imageName);

                UploadTask uploadTask = storageRef.child(imageName).putStream(stream);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...


                    }
                });


            } catch (FileNotFoundException ex) {
//                e.printStackTrace();
            }


        }
    });





}

                }








                List<Account> accounts1 =  AppDatabase.getsInstance(this).accountDao().loadAllAccountsList();

                for (int i=0;i<accounts1.size();i++)
                {

                    map=new HashMap<>();
                    map.put("id",accounts1.get(i).getId());
                    map.put("name",accounts1.get(i).getName());
                    map.put("icon",accounts1.get(i).getIcon());
                    map.put("total",accounts1.get(i).getTotal());
                    map.put("defaultAccount",accounts1.get(i).getDefaultAccount());


                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Accounts").child(String.valueOf(accounts1.get(i).getId())).setValue(map);
                }


                List<Category>  categories =  AppDatabase.getsInstance(this).categoryDao().loadAllCategoriesList();


                for (int i=0;i<categories.size();i++)
                {

                    map=new HashMap<>();
                    map.put("id",categories.get(i).getId());
                    map.put("name",categories.get(i).getName());
                    map.put("icon",categories.get(i).getIcon());
                    map.put("type",categories.get(i).getType());
                    map.put("defaultCategory",categories.get(i).getDefaultCategory());
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Categories").child(String.valueOf(categories.get(i).getId())).setValue(map);
                }


        List<Sequence>  sequences =  AppDatabase.getsInstance(this).sequenceDao().loadAllSequenceAllList();


        for (int i=0;i<sequences.size();i++)
        {

            map=new HashMap<>();
            map.put("id",sequences.get(i).getId());
            map.put("name",sequences.get(i).getName());
            map.put("seq",sequences.get(i).getSeq());
            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Sequence").child(String.valueOf(sequences.get(i).getId())).setValue(map);
        }



        Sync sync =  AppDatabase.getsInstance(this).syncDao().loadSyncData();

        map=new HashMap<>();
        map.put("id",sync.getId());
        map.put("date",sync.getDate());
        map.put("status",sync.getStatus());
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Sync").setValue(map);




      // if (status)
      //SignOutandFinish();






    }

    String GetUserId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Id = preferences.getString("Id", "");

        return Id;

    }



    void SignOutandFinish(){


        /*
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("Id");
        editor.remove("Email");
        editor.remove("Sync");

        editor.apply();

        AppDatabase.getsInstance(this).categoryDao().DeleteTable();
        AppDatabase.getsInstance(this).accountDao().DeleteTable();
        AppDatabase.getsInstance(this).expenseDao().DeleteTable();
        AppDatabase.getsInstance(this).sequenceDao().DeleteTable();
        AppDatabase.getsInstance(this).syncDao().DeleteTable();
*/





        FirebaseAuth.getInstance().signOut();

//AppDatabase.getsInstance(this).clearAllTables();

    }





}
