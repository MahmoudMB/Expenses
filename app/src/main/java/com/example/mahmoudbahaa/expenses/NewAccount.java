package com.example.mahmoudbahaa.expenses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Account;
import com.example.mahmoudbahaa.expenses.models.Category;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewAccount extends AppCompatActivity {



    @BindView(R.id.NewAccount_name)
    EditText AccountName;


    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());

    }



    @OnClick(R.id.NewAccount_cancel)
    void Cancel(){
        finish();
    }


    @OnClick(R.id.NewAccount_Done)
    void OnDone()
    {
        final String accountName = AccountName.getText().toString();
        if (TextUtils.isEmpty(accountName))
        {

        }
        else{

            final Account account = new Account(accountName,"ic_baseline_account_balance_24px");

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.accountDao().insertAccount(account);
                    finish();

                }
            });
        }
    }




}
