package com.mahmoudbahaa.wallet;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mahmoudbahaa.wallet.data.AppDatabase;
import com.mahmoudbahaa.wallet.models.Account;
import com.mahmoudbahaa.wallet.models.Sequence;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewAccount extends AppCompatActivity {



    @BindView(R.id.NewAccount_name)
    EditText AccountName;


@BindView(R.id.Total_TextLabel)
TextView Total_TextLabel;


    @BindView(R.id.NewAccount_price)
    EditText AccountTotal;


    @BindView(R.id.newAccount_ChosenColor)
    ImageView ChosenColor;


    @BindView(R.id.newAccount_ColorsPlate)
    LinearLayout ColorsPlate;

    private AppDatabase mDb;

int CurrentColorIndex = 0;

    List<RelativeLayout> colors = new ArrayList<>(9);
    List<ImageView> checks = new ArrayList<>(9);


    List<String> ColorsHexa = new ArrayList<>(9);





    @BindView(R.id.AddActivity_FirstLayout)
        LinearLayout AddActivity_FirstLayout;

    @BindView(R.id.AddActivity_SecondLayout)
    LinearLayout AddActivity_SecondLayout;



    Account accountObj;
    Boolean Update = false;

    @BindView(R.id.NewAccount_Title)
    TextView NewAccount_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());
        initColors();

        if (getIntent().hasExtra("Account"))
        {

            Total_TextLabel.setText("اجمالي المبلغ الحالي");
            NewAccount_Title.setText("تعديل حساب");
            Update = true;
            accountObj = (Account) getIntent().getSerializableExtra("Account");
            AccountName.setText(accountObj.getName());
            AccountTotal.setText(accountObj.getTotal()+"");

            AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(accountObj.getIcon()));
            AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(accountObj.getIcon()));




            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                ChosenColor.setBackgroundColor(getResources().getColor(ReturnColor(accountObj.getIcon())));


            else
                ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), ReturnColor(accountObj.getIcon())));

        }



    }


    @OnClick(R.id.NewAccount_ChooseColorLayout)
    void OnClickOnChooseColor(){


        if (ColorsPlate.getTag().equals("false")) {
            ColorsPlate.setVisibility(View.VISIBLE);
            ColorsPlate.setTag("true");
        }
        else {
            ColorsPlate.setVisibility(View.GONE);
            ColorsPlate.setTag("false");

        }

    }



    @OnClick(R.id.NewAccount_cancel)
    void Cancel(){
        finish();
    }


    @OnClick(R.id.NewAccount_Done)
    void OnDone()
    {
        final String accountName = AccountName.getText().toString();
        float accountTotal  = 0;
        if (!TextUtils.isEmpty(AccountTotal.getText().toString()))
          accountTotal = Float.parseFloat( AccountTotal.getText().toString());

        if (TextUtils.isEmpty(accountName) )
        {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"ادخل اسم الحساب", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else{

            if (!Update) {

                Sequence seq = mDb.sequenceDao().loadAccountSeq();
                seq.setSeq(seq.getSeq() + 1);

                final Account account = new Account(seq.getSeq(), accountName, accountTotal, ColorsHexa.get(CurrentColorIndex), false);

                final Sequence seqUpdated = seq;
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.sequenceDao().UpdateAccountSeq(seqUpdated);
                        mDb.accountDao().insertAccount(account);
                        finish();



                    }
                });
                
            }


            else if (Update){

                if (!accountObj.getName().equals(accountName) || !accountObj.getIcon().equals(ColorsHexa.get(CurrentColorIndex)) || accountObj.getTotal() != accountTotal){

                    final Account account = new Account(accountObj.getId(), accountName,accountTotal  ,ColorsHexa.get(CurrentColorIndex), accountObj.getDefaultAccount());

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            mDb.accountDao().UpdateAccount(account);

                            finish();

                        }
                    });
                }
                finish();

            }



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




        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);
        checks.get(0).setVisibility(View.VISIBLE);

        CurrentColorIndex = 0;

        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(0)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(0)));




        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
        ChosenColor.setBackgroundColor(getResources().getColor(R.color.color1));


        else
            ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color1));




    }

    @OnClick(R.id.Color2_Relative)
    void On2colorClick()
    {



        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);
        checks.get(1).setVisibility(View.VISIBLE);


        CurrentColorIndex = 1;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(1)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(1)));

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            ChosenColor.setBackgroundColor(getResources().getColor(R.color.color2));


        else
            ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color2));


    }

    @OnClick(R.id.Color3_Relative)
    void On3colorClick()
    {



        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);
        checks.get(2).setVisibility(View.VISIBLE);


        CurrentColorIndex = 2;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(2)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(2)));

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            ChosenColor.setBackgroundColor(getResources().getColor(R.color.color3));


        else
            ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color3));


    }

    @OnClick(R.id.Color4_Relative)
    void On4colorClick()
    {



        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);
        checks.get(3).setVisibility(View.VISIBLE);


        CurrentColorIndex = 3;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(3)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(3)));

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            ChosenColor.setBackgroundColor(getResources().getColor(R.color.color4));


        else
            ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color4));


    }

    @OnClick(R.id.Color5_Relative)
    void On5colorClick()
    {



        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);
        checks.get(4).setVisibility(View.VISIBLE);


        CurrentColorIndex = 4;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(4)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(4)));

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            ChosenColor.setBackgroundColor(getResources().getColor(R.color.color5));


        else
            ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color5));


    }



    @OnClick(R.id.Color6_Relative)
    void On6colorClick()
    {



        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);
        checks.get(5).setVisibility(View.VISIBLE);


        CurrentColorIndex = 5;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(5)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(5)));


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            ChosenColor.setBackgroundColor(getResources().getColor(R.color.color6));


        else
            ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color6));

    }

    @OnClick(R.id.Color7_Relative)
    void On7colorClick()
    {



        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);
        checks.get(6).setVisibility(View.VISIBLE);


        CurrentColorIndex = 6;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(6)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(6)));

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            ChosenColor.setBackgroundColor(getResources().getColor(R.color.color7));


        else
            ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color7));


    }

    @OnClick(R.id.Color8_Relative)
    void On8colorClick()
    {



        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);
        checks.get(7).setVisibility(View.VISIBLE);

        CurrentColorIndex = 7;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(7)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(7)));

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            ChosenColor.setBackgroundColor(getResources().getColor(R.color.color8));


        else
            ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color8));


    }

    @OnClick(R.id.Color9_Relative)
            void On9colorClick()
    {


        checks.get(CurrentColorIndex).setVisibility(View.INVISIBLE);
        checks.get(8).setVisibility(View.VISIBLE);

        CurrentColorIndex = 8;
        AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(8)));
        AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(ColorsHexa.get(8)));

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            ChosenColor.setBackgroundColor(getResources().getColor(R.color.color9));


        else
            ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color9));

    }


    int ReturnColor(String color){

        switch (color){

            case "#8281ff":
                CurrentColorIndex = 0;
                return R.color.color1;

            case "#639af4":
                CurrentColorIndex = 1;
                return R.color.color2;


            case "#7ad2ff":
                CurrentColorIndex = 2;
                return R.color.color3;


            case "#4cd3b2":
                CurrentColorIndex = 3;
                return R.color.color4;

            case "#47d469":
                CurrentColorIndex = 4;
                return R.color.color5;

            case "#f2be44":
                CurrentColorIndex = 5;
                return R.color.color6;

            case "#ff965d":
                CurrentColorIndex = 6;
                return R.color.color7;


            case "#fd7881":
                CurrentColorIndex = 7;
                return R.color.color8;

            case "#d38cf2":
                CurrentColorIndex = 8;
                return R.color.color9;

        }
        return 0;
    }



}
