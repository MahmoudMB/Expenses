package com.mahmoudbahaa.expenses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.mahmoudbahaa.expenses.data.AppDatabase;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {




@BindView(R.id.Login_Email)
    EditText Email;

    @BindView(R.id.Login_Password)
    EditText Pasword;


    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());

    }



    @OnClick(R.id.Register_SignUp)
    void OnSignUpCLick(){

final String EmailStr =  Email.getText().toString();
final String PAsswordStr =  Pasword.getText().toString();

        if (TextUtils.isEmpty( EmailStr) ||TextUtils.isEmpty(PAsswordStr))
        {

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"ادخل البريد وكلمة السر", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        else {

            UserService.getInstance(getApplicationContext()).createNewUser(EmailStr, PAsswordStr, new OnEventListener<String>() {
                @Override
                public void onSuccess(String object) throws JSONException {

                    Log.v("Sucess",object);
                    writeInSharedPrefs(object,EmailStr);

                    startMainActivity("SignUp");
                    finish();

                }

                @Override
                public void onFailure(Exception e) {
                     Log.v("Failure",e.getMessage()+"");

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                @Override
                public void FailureMessage(String m) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), m, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });


        }

    }



    @OnClick(R.id.Register_SignIn)
    void OnSignInClick(){
        finish();
    }



    void startMainActivity(String status){

        Intent i = new Intent(Register.this,MainActivity.class);
        i.putExtra("status",status);
        startActivity(i);
        finish();
    }

    public void writeInSharedPrefs(String userId,String Email)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Id",userId);
        editor.putString("Email",Email);
        editor.putString("Sync","ON");
        editor.putString("LoggingStatus","TRUE");
        editor.apply();

    }





}
