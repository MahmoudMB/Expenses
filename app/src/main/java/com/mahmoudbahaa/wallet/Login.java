package com.mahmoudbahaa.wallet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mahmoudbahaa.wallet.data.AppDatabase;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {


    Boolean SignIn = true;


    @BindView(R.id.Login_SignIn)
    Button SignInBtn;

    @BindView(R.id.Login_SignUp)
    Button SignUpBtn;



    @BindView(R.id.Login_Email)
    EditText Email;

    @BindView(R.id.Login_Password)
    EditText Pasword;



    @BindView(R.id.Login_StatusText)
    TextView LoginStatusText;


    @BindView(R.id.Login_Btns)
    RelativeLayout BtnsLayout;


    @BindView(R.id.Login_Prograss)
    ProgressBar Login_Prograss;



    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        CheckIfUserLoggedIn();
        mDb = AppDatabase.getsInstance(getApplicationContext());

    }






    void CheckIfUserLoggedIn(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String LoggingStatus = preferences.getString("LoggingStatus", "");

        if (!TextUtils.isEmpty(LoggingStatus)&&LoggingStatus.equals("TRUE")) {
            startMainActivity("Normal");
            finish();
        }

    }



    void startMainActivity(String status){

        Intent i = new Intent(Login.this,MainActivity.class);
        i.putExtra("status",status);
        startActivity(i);
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



    String GetUserId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Id = preferences.getString("Id", "");


        return Id;

    }


    @OnClick(R.id.Login_SignIn)
    void OnSignInClick(){

        LoginStatusText.setText("تسجيل الدخول");
        SignIn = true;


           // SignInBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.login_corner_active));
          //  SignUpBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.login_corner_inactive));

       // SignInBtn.setText("تسجيل الدخول");
       // SignUpBtn.setText("تسجيل الان");



        final String EmailStr =  Email.getText().toString();
        final String PAsswordStr =  Pasword.getText().toString();

        if (TextUtils.isEmpty( EmailStr) ||TextUtils.isEmpty(PAsswordStr))
        {


            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"ادخل البريد وكلمة السر", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
        else{

        //    CleanDb();
         //   CleanSharedPrefs();

            BtnsLayout.setVisibility(View.GONE);
            Login_Prograss.setVisibility(View.VISIBLE);


            UserService.getInstance(getApplicationContext()).SignIn(Email.getText().toString(), Pasword.getText().toString(), new OnEventListener<String>() {
                @Override
                public void onSuccess(String object) throws JSONException {
                    Log.v("Sucess",object);




                    if (Email.length() > 0) {
                        Email.getText().clear();
                    }
                    if (Pasword.length() > 0) {
                        Pasword.getText().clear();
                    }



                    if (!TextUtils.isEmpty(GetOldEmailFromShared()))
                    {
                        if (GetOldEmailFromShared().equals(EmailStr))
                        {
                            String status = "LogIn";
                            Boolean sameUser = true;

                            Intent i = new Intent(Login.this,MainActivity.class);
                            i.putExtra("status",status);
                            i.putExtra("sameUser",sameUser);
                            startActivity(i);

                            finish();
                        }
                        else {


                            CleanSharedPrefs();
                            writeInSharedPrefs(object,EmailStr);
                            String Status = "LogIn";
                            Boolean sameUser = false;

                            Intent i = new Intent(Login.this,MainActivity.class);
                            i.putExtra("status",Status);
                            i.putExtra("sameUser",sameUser);
                            startActivity(i);

                            finish();

                        }


                    }

                    else{

                        CleanSharedPrefs();
                        writeInSharedPrefs(object,EmailStr);
                        String Status = "LogIn";
                        Boolean sameUser = false;

                        Intent i = new Intent(Login.this,MainActivity.class);
                        i.putExtra("status",Status);
                        i.putExtra("sameUser",sameUser);
                        startActivity(i);

                        finish();

                    }

                }

                @Override
                public void onFailure(Exception e) {
                    BtnsLayout.setVisibility(View.VISIBLE);
                    Login_Prograss.setVisibility(View.GONE);

                    Log.v("Failure",e.getMessage()+"");

                 //   Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_LONG);
                 //   snackbar.show();
                }

                @Override
                public void FailureMessage(String m) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),m, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });

        }

    }


    void CleanDb(){



        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.categoryDao().DeleteTable();
                mDb.accountDao().DeleteTable();
                mDb.expenseDao().DeleteTable();

            }


        });

    }
    void CleanSharedPrefs(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("Id");
        editor.remove("Email");
        editor.remove("Sync");
        editor.remove("LoggingStatus");
        editor.apply();
    }


    String GetOldEmailFromShared()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Email = preferences.getString("Email", "");


        return Email;
    }



/*
    @OnClick(R.id.Login_SignUp)
    void OnSignUpClick(){

        SignIn = false;


      //  SignInBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.login_corner_active));
       //  SignUpBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.login_corner_active));

     //   SignInBtn.setText("تسجيل الان");
     //   SignUpBtn.setText("تسجيل الدخول");

        Intent i = new Intent(Login.this,Register.class);
        startActivity(i);


    }

*/



    @OnClick(R.id.Login_SignUp)
    void OnSignUpCLick(){




        LoginStatusText.setText("تسجيل جديد");
        final String EmailStr =  Email.getText().toString();
        final String PAsswordStr =  Pasword.getText().toString();

        if (TextUtils.isEmpty( EmailStr) ||TextUtils.isEmpty(PAsswordStr))
        {



            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"ادخل البريد وكلمة السر للتسجيل", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        else {

            BtnsLayout.setVisibility(View.GONE);
            Login_Prograss.setVisibility(View.VISIBLE);

            UserService.getInstance(getApplicationContext()).createNewUser(EmailStr, PAsswordStr, new OnEventListener<String>() {
                @Override
                public void onSuccess(String object) throws JSONException {



                    if (Email.length() > 0) {
                        Email.getText().clear();
                    }
                    if (Pasword.length() > 0) {
                        Pasword.getText().clear();
                    }


                    BtnsLayout.setVisibility(View.VISIBLE);
                    Login_Prograss.setVisibility(View.GONE);

                    Log.v("Sucess",object);
                    writeInSharedPrefs(object,EmailStr);

                    startMainActivity("SignUp");
                    finish();

                }

                @Override
                public void onFailure(Exception e) {
                    BtnsLayout.setVisibility(View.VISIBLE);
                    Login_Prograss.setVisibility(View.GONE);


                    Log.v("Failure",e.getMessage()+"");

              //      Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_LONG);
               //     snackbar.show();
                }

                @Override
                public void FailureMessage(String m) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), m, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });


        }

    }


    @OnClick(R.id.Login_Forgot)
    void OnForgotPAssword(){

        Intent i = new Intent(Login.this,ResetPassword.class);
        startActivity(i);
    }


}
