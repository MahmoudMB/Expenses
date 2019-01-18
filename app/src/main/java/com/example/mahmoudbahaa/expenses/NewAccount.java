package com.example.mahmoudbahaa.expenses;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Account;
import com.example.mahmoudbahaa.expenses.models.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewAccount extends AppCompatActivity {



    @BindView(R.id.NewAccount_name)
    EditText AccountName;





    @BindView(R.id.NewAccount_price)
    EditText AccountTotal;


    private AppDatabase mDb;

int CurrentColorIndex = 0;

    List<RelativeLayout> colors = new ArrayList<>(9);
    List<ImageView> checks = new ArrayList<>(9);


    List<String> ColorsHexa = new ArrayList<>(9);





    @BindView(R.id.AddActivity_FirstLayout)
        LinearLayout AddActivity_FirstLayout;

    @BindView(R.id.AddActivity_SecondLayout)
    LinearLayout AddActivity_SecondLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());
        initColors();

    }



    @OnClick(R.id.NewAccount_cancel)
    void Cancel(){
        finish();
    }


    @OnClick(R.id.NewAccount_Done)
    void OnDone()
    {
        final String accountName = AccountName.getText().toString();
        if (TextUtils.isEmpty(accountName) )
        {

        }
        else{

            final Account account = new Account(accountName,Float.parseFloat(AccountTotal.getText().toString()),ColorsHexa.get(CurrentColorIndex),false);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.accountDao().insertAccount(account);
                    finish();

                }
            });
        }
    }


    void initColors(){


     colors.add((RelativeLayout) findViewById(R.id.Color1_Relative));
        colors.add((RelativeLayout) findViewById(R.id.Color2_Relative));
        colors.add((RelativeLayout) findViewById(R.id.Color3_Relative));
        colors.add((RelativeLayout) findViewById(R.id.Color4_Relative));
        colors.add((RelativeLayout) findViewById(R.id.Color5_Relative));
        colors.add((RelativeLayout) findViewById(R.id.Color6_Relative));
        colors.add((RelativeLayout) findViewById(R.id.Color7_Relative));
        colors.add((RelativeLayout) findViewById(R.id.Color8_Relative));
        colors.add((RelativeLayout) findViewById(R.id.Color9_Relative));



        checks.add((ImageView)findViewById(R.id.Color1_check));
        checks.add((ImageView)findViewById(R.id.Color2_check));
        checks.add((ImageView)findViewById(R.id.Color3_check));
        checks.add((ImageView)findViewById(R.id.Color4_check));
        checks.add((ImageView)findViewById(R.id.Color5_check));
        checks.add((ImageView)findViewById(R.id.Color6_check));
        checks.add((ImageView)findViewById(R.id.Color7_check));
        checks.add((ImageView)findViewById(R.id.Color8_check));
        checks.add((ImageView)findViewById(R.id.Color9_check));




        ColorsHexa.add("#8281ff");
        ColorsHexa.add("#639af4");
        ColorsHexa.add("#7ad2ff");
        ColorsHexa.add("#4cd3b2");
        ColorsHexa.add("#47d469");
        ColorsHexa.add("#f2be44");
        ColorsHexa.add("#ff965d");
        ColorsHexa.add("#fd7881");
        ColorsHexa.add("#d38cf2");


    }


    @OnClick(R.id.Color1_Relative)
    void On1colorClick()
    {


        checks.get(0).setVisibility(View.VISIBLE);

        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);

        CurrentColorIndex = 0;

        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(0)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(0)));

    }

    @OnClick(R.id.Color2_Relative)
    void On2colorClick()
    {


        checks.get(1).setVisibility(View.VISIBLE);

        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);

        CurrentColorIndex = 1;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(1)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(1)));

    }

    @OnClick(R.id.Color3_Relative)
    void On3colorClick()
    {


        checks.get(2).setVisibility(View.VISIBLE);

        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);

        CurrentColorIndex = 2;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(2)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(2)));

    }

    @OnClick(R.id.Color4_Relative)
    void On4colorClick()
    {


        checks.get(3).setVisibility(View.VISIBLE);

        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);

        CurrentColorIndex = 3;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(3)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(3)));

    }

    @OnClick(R.id.Color5_Relative)
    void On5colorClick()
    {


        checks.get(4).setVisibility(View.VISIBLE);

        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);

        CurrentColorIndex = 4;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(4)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(4)));

    }



    @OnClick(R.id.Color6_Relative)
    void On6colorClick()
    {


        checks.get(5).setVisibility(View.VISIBLE);

        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);

        CurrentColorIndex = 5;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(5)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(5)));
    }

    @OnClick(R.id.Color7_Relative)
    void On7colorClick()
    {


        checks.get(6).setVisibility(View.VISIBLE);

        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);

        CurrentColorIndex = 6;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(6)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(6)));

    }

    @OnClick(R.id.Color8_Relative)
    void On8colorClick()
    {


        checks.get(7).setVisibility(View.VISIBLE);

        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);

        CurrentColorIndex = 7;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(7)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(7)));

    }

    @OnClick(R.id.Color9_Relative)
            void On9colorClick()
    {

        checks.get(8).setVisibility(View.VISIBLE);

        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);

        CurrentColorIndex = 8;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(8)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(8)));
    }


}
