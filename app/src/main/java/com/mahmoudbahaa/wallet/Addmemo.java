package com.mahmoudbahaa.wallet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Addmemo extends AppCompatActivity {


    @BindView(R.id.AddMemo_memo)
    EditText memoText;
    String Memo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmemo);

        ButterKnife.bind(this);


         if (getIntent().hasExtra("Memo")) {
             Memo = getIntent().getStringExtra("Memo");
             memoText.setText(Memo);
         }
    }






    @OnClick(R.id.AddMemo_Done)
    void OnDone(){

        String memo = memoText.getText().toString();


        if (!TextUtils.isEmpty(memo)) {
            Intent data = new Intent();
            // add data to Intent
            data.putExtra("Memo", memo);

            setResult(Activity.RESULT_OK, data);
        }

        else {

            setResult(Activity.RESULT_CANCELED);
        }


        finish();


    }


    @OnClick(R.id.AddMemo_Cancel)
    void OnCencell(){

        finish();

    }



}
