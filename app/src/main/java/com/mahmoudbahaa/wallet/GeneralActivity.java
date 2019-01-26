package com.mahmoudbahaa.wallet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mahmoudbahaa.wallet.data.AppDatabase;
import com.mahmoudbahaa.wallet.models.Account;
import com.mahmoudbahaa.wallet.models.Category;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GeneralActivity extends AppCompatActivity {


    @BindView(R.id.General_AccountText)
    TextView AccountText;

    @BindView(R.id.General_CategoryOutcomeText)
    TextView CategoryOutcomeText;


    @BindView(R.id.General_CategoryIncomeText)
    TextView CategoryIncomeText;

    private AppDatabase mDb;


    public Category categoryOutcomeObj;
    public Category categoryIncomeObj;

    public Account accountObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());
        LoadDefaultAccount();
        LoadDefaultCategory();
    }



void LoadDefaultAccount()
{


    LiveData<Account> account   = mDb.accountDao().LoadDefaultAccount(true);

    account.observe(this, new Observer<Account>() {
        @Override
        public void onChanged(@Nullable Account account1) {

            accountObj = account1;
            AccountText.setText(account1.getName());
        }
    });


}


void LoadDefaultCategory()
{



    LiveData<Category> categoryOutcome   = mDb.categoryDao().LoadDefaultOutCome(true);

    categoryOutcome.observe(this, new Observer<Category>() {
        @Override
        public void onChanged(@Nullable Category category1) {

            categoryOutcomeObj = category1;
            CategoryOutcomeText.setText(category1.getName());
        }
    });


    LiveData<Category> categoryIncome   = mDb.categoryDao().LoadDefaultIncome(true);

    categoryIncome.observe(this, new Observer<Category>() {
        @Override
        public void onChanged(@Nullable Category category1) {

            categoryIncomeObj = category1;
            CategoryIncomeText.setText(category1.getName());
        }
    });


}


    @OnClick(R.id.General_Account)
    void OpenAccounts(){


        Intent i = new Intent(GeneralActivity.this,EditAccount.class);
        i.putExtra("ScreenType","General");
        i.putExtra("Account",accountObj);
        startActivity(i);


    }


    @OnClick(R.id.General_CategoryOutcome)
    void OpenCategoryOutcome(){

        Intent i = new Intent(GeneralActivity.this,EditCategoryOutcome.class);
        i.putExtra("Type","Outcome");
        i.putExtra("ScreenType","General");
        i.putExtra("Category",categoryOutcomeObj);

        startActivity(i);

    }




    @OnClick(R.id.General_CategoryIncome)
    void OpenCategoryIncome(){


        Intent i = new Intent(GeneralActivity.this,EditCategoryIncome.class);
        i.putExtra("Type","Income");
        i.putExtra("Category",categoryIncomeObj);
        i.putExtra("ScreenType","General");
        startActivity(i);


    }

    @OnClick(R.id.AccountEdit_back)
    void back(){
        finish();
    }

}
