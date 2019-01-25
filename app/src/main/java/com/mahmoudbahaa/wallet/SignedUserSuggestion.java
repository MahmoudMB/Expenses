package com.mahmoudbahaa.expenses;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignedUserSuggestion extends AppCompatActivity {

    @BindView(R.id.Contact_Cancel)
    TextView Cancel;

    @BindView(R.id.Contact_Msg)
    EditText Msg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_user_suggestion);
        ButterKnife.bind(this);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    @OnClick(R.id.Contact_Send)
    void SendMessage(){

        String message = Msg.getText().toString();
        if (!TextUtils.isEmpty(message))
        {

String userId = GetUserId();
           Map map=new HashMap<>();
           // map.put("Uid",userId);
            map.put("Msg",message);

         String key =    FirebaseDatabase.getInstance().getReference().child("ContactUs").child(userId).push().getKey();
            FirebaseDatabase.getInstance().getReference().child("ContactUs").child(userId).child(key).setValue(map);
            Toast.makeText(getApplicationContext(),"تم ارسال الرسالة بنجاح",Toast.LENGTH_SHORT).show();

            finish();

        }
        else{
            Toast.makeText(getApplicationContext(),"يرجى ادخال رسالة",Toast.LENGTH_SHORT).show();
        }


    }


    String GetUserId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Id = preferences.getString("Id", "");


        return Id;

    }




}
