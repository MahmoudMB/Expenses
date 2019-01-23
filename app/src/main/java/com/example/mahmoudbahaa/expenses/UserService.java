package com.example.mahmoudbahaa.expenses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MahmoudBahaa on 19/01/2019.
 */

public class UserService {


    private Context context;
    private  static UserService instance;


    private DatabaseReference mUserReference;
    private static FirebaseAuth mAuth;


    private UserService(Context context) {

        this.context = context;
    }



    public static synchronized UserService getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new UserService(context);

        }

        return instance;
    }


    public  void createNewUser(String Email,String Pass,final OnEventListener<String> callback)
    {
        Log.v("Result1",Email);
        mAuth = FirebaseAuth.getInstance();
        final boolean[] result = new boolean[1];
        mAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Map<String,Object> map=new HashMap<>();
                    map.put("Uid",user.getUid());
                    map.put("Email",user.getEmail());
                    String keyToPush= mAuth.getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(keyToPush).child("Info").setValue(map);
                    result[0] = true;

                    //mUsersCallbacks.CheckStatusOfSignUp(true,"Succefful Sign Up");


                    try {
                        callback.onSuccess(user.getUid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Log.d("failed", "onComplete: Failed=" + task.getException().getMessage()); //ADD THIS

                    result[0] = false;
                    Log.v("Result","not succeful");

                   // mUsersCallbacks.CheckStatusOfSignUp(false,task.getException().getMessage());
                    callback.onFailure(task.getException());
                }

            }
        });








//return result[0];

    }





    public  void SignIn(String Email,String Pass,final OnEventListener<String> callback)
    {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sign in", "signInWithEmail:success");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    try {
                        callback.onSuccess(user.getUid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sign in", "signInWithEmail:failure", task.getException());

callback.onFailure(task.getException());                }

            }
        });

    }







}
