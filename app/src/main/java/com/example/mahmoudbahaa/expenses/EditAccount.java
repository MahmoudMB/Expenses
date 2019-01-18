package com.example.mahmoudbahaa.expenses;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.mahmoudbahaa.expenses.adapters.AccountAdapter;
import com.example.mahmoudbahaa.expenses.adapters.CategoryAdapter;
import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Account;
import com.example.mahmoudbahaa.expenses.models.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditAccount extends AppCompatActivity implements AccountAdapter.ListItemClickListener {



    AccountAdapter accountAdapter;
    List<Account> accounts = new ArrayList<>();

    @BindView(R.id.AccountEdit_Recycler)
    RecyclerView recyclerView;




    Account accountObj;
    Boolean Select = false;
    String ScreenType = "";
    Boolean FirstRun = true;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);
        FirstRun = true;

        if (savedInstanceState!=null)
            FirstRun =    savedInstanceState.getBoolean("FirstRun");


        mDb = AppDatabase.getsInstance(getApplicationContext());

        initAdapter();

    //    insertDummyAccounts();


        if (getIntent().hasExtra("Account")) {
            accountObj = (Account) getIntent().getSerializableExtra("Account");
            Select = true;
            ScreenType = getIntent().getStringExtra("ScreenType");

            if (accountObj != null && accounts.size()>0)
            {
                int Index =  accounts.indexOf(accountObj);
                accounts.get(Index).setStatus(true);
                accounts.get(0).setStatus(false);
                accountAdapter.notifyDataSetChanged();
            }

        }

        LoadAccounts();

    }





    void initAdapter(){

        accountAdapter = new AccountAdapter(getApplicationContext(),accounts,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(accountAdapter);

    }

    void insertDummyAccounts(){


        Account a = new Account();

        a.setStatus(true);
        a.setId(1);
        a.setName("الحساب الاساسي");
        a.setIcon("ic_baseline_account_balance_24px");
        accounts.add(a);


        a = new Account();

        a.setStatus(false);
        a.setId(2);
        a.setName("حساب الراجحي");
        a.setIcon("ic_baseline_account_balance_24px");
        accounts.add(a);



        a = new Account();

        a.setStatus(false);
        a.setId(3);
        a.setName("حساب الاهلي");
        a.setIcon("ic_baseline_account_balance_24px");
        accounts.add(a);

accountAdapter.notifyDataSetChanged();


    }





    void LoadAccounts()
    {

        LiveData<List<Account>> c = mDb.accountDao().loadAllAccounts();

        c.observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> accounts1) {

                accounts.clear();
                accounts.addAll(accounts1);
                accountAdapter.notifyDataSetChanged();



                if (accountObj != null)
                {
                    if (FirstRun){

                        int Index =  accounts.indexOf(accountObj);
                        accounts.get(Index).setStatus(true);

                        accountAdapter.notifyDataSetChanged();
                    }
                }




            }
        });

    }



    @Override
    public void onListItemClick(int clickedItemIndex) {

        FirstRun = false;


        if (Select)
        {

            switch (ScreenType){

                case "General":

                        if (!accountObj.equals(accounts.get(clickedItemIndex))) {

                            int index = accounts.indexOf(accountObj);
                            accounts.get(index).setDefaultAccount(false);
                            accounts.get(clickedItemIndex).setDefaultAccount(true);

                            accounts.get(index).setStatus(false);
                            accounts.get(clickedItemIndex).setStatus(true);

                            accountAdapter.notifyDataSetChanged();

                            changeAccount(accounts.get(index));
                            changeAccount(accounts.get(clickedItemIndex));
                            finish();
                        }

                    finish();
                    break;

                case "Add":
                    Intent data = new Intent();

                        if (!accountObj.equals(accounts.get(clickedItemIndex))) {

                            int index = accounts.indexOf(accountObj);
                            accounts.get(index).setStatus(false);
                            accounts.get(clickedItemIndex).setStatus(true);
                            accountAdapter.notifyDataSetChanged();

                        }
                        data.putExtra("Account", accounts.get(clickedItemIndex));

                        setResult(Activity.RESULT_OK, data);

                        finish();


                    break;



            }



        }


    }

    void changeAccount(final Account c){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.accountDao().UpdateAccount(c);
            }
        });

    }


    @OnClick(R.id.AccountEdit_back)
    void OnBack(){
        finish();
    }

    @OnClick(R.id.AccountEdit_AddNew)
    void AddNewAccount(){

        Intent i  = new Intent(EditAccount.this,NewAccount.class);
        startActivity(i);

    }





    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("FirstRun",FirstRun);
    }




}
