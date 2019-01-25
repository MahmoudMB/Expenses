package com.mahmoudbahaa.expenses;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPassword extends AppCompatActivity {

    @BindView(R.id.Login_Email)
    EditText EmailText;

    @BindView(R.id.Login_Btns)
    RelativeLayout BtnsLayout;


    @BindView(R.id.Login_Prograss)
    ProgressBar Login_Prograss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);



    }


    @OnClick(R.id.Login_SignIn)
    void SendPassword(){


        String Email = EmailText.getText().toString();

        if (!TextUtils.isEmpty(Email))
        {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            BtnsLayout.setVisibility(View.GONE);
            Login_Prograss.setVisibility(View.VISIBLE);

            auth.sendPasswordResetEmail(Email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"تم ارسال رابط تغيير الباسوورد على البريد  الالكتروني",Toast.LENGTH_SHORT).show();
                            finish();
                            }
                            else {
                                BtnsLayout.setVisibility(View.VISIBLE);
                                Login_Prograss.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"هذا البريد غير مسجل في النظام",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        else {
            Toast.makeText(getApplicationContext(),"ادخل البريد الالكتروني",Toast.LENGTH_SHORT).show();

        }


    }

    @OnClick(R.id.reset_back)
    void ResetBack(){
        finish();
    }
}
