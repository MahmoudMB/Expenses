package com.mahmoudbahaa.wallet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
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

                    try {
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                        callback.FailureMessage(ReturnArabicMessage(errorCode));

                    }
                  catch (Exception ex){

                      callback.FailureMessage(task.getException().getMessage());

                  }

                    result[0] = false;



                   // mUsersCallbacks.CheckStatusOfSignUp(false,task.getException().getMessage());
                    callback.onFailure(task.getException());
                }

            }
        });








//return result[0];

    }



    public String ReturnArabicMessage(String errorCode){


        switch (errorCode) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                return "The custom token format is incorrect";


            case "ERROR_CUSTOM_TOKEN_MISMATCH":
return "The custom token corresponds to a different audience";

            case "ERROR_INVALID_CREDENTIAL":
                return context.getResources().getString(R.string.ERROR_INVALID_CREDENTIAL);


            case "ERROR_INVALID_EMAIL":
                return context.getResources().getString(R.string.ERROR_INVALID_EMAIL);


            case "ERROR_WRONG_PASSWORD":

                return context.getResources().getString(R.string.ERROR_WRONG_PASSWORD);


            case "ERROR_USER_MISMATCH":
return "The supplied credentials do not correspond to the previously signed in user";

            case "ERROR_REQUIRES_RECENT_LOGIN":
                return context.getResources().getString(R.string.ERROR_REQUIRES_RECENT_LOGIN);

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                return context.getResources().getString(R.string.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL);

            case "ERROR_EMAIL_ALREADY_IN_USE":
                return context.getResources().getString(R.string.ERROR_EMAIL_ALREADY_IN_USE);


            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
return "This credential is already associated with a different user account";

            case "ERROR_USER_DISABLED":
                return context.getResources().getString(R.string.ERROR_USER_DISABLED);

            case "ERROR_USER_TOKEN_EXPIRED":
                return context.getResources().getString(R.string.ERROR_USER_TOKEN_EXPIRED);


            case "ERROR_USER_NOT_FOUND":
return context.getResources().getString(R.string.ERROR_USER_NOT_FOUND);

            case "ERROR_INVALID_USER_TOKEN":
                return context.getResources().getString(R.string.ERROR_INVALID_USER_TOKEN);

            case "ERROR_OPERATION_NOT_ALLOWED":
                return context.getResources().getString(R.string.ERROR_OPERATION_NOT_ALLOWED);

            case "ERROR_WEAK_PASSWORD":
                return context.getResources().getString(R.string.ERROR_WEAK_PASSWORD);


            default:
                return "يوجد خطأ";

        }


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


                    try {
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                        callback.FailureMessage(ReturnArabicMessage(errorCode));

                    }
                    catch (Exception ex){

                        callback.FailureMessage(task.getException().getMessage());

                    }

callback.onFailure(task.getException());


                }

            }
        });

    }







}
