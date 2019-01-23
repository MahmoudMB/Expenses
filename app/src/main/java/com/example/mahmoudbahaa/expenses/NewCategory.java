package com.example.mahmoudbahaa.expenses;

import android.graphics.Color;
import android.os.Build;
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

import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Category;
import com.example.mahmoudbahaa.expenses.models.Sequence;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewCategory extends AppCompatActivity {
/*

     @BindView(R.id.Add_Income_Text)
    TextView Add_Income_Text;
    @BindView(R.id.Add_Income)
    LinearLayout Add_Income;

@BindView(R.id.Add_OutCome)
LinearLayout Add_OutCome;



*/

    @BindView(R.id.Add_OutCome_Text)
    TextView Add_OutCome_Text;



    @BindView(R.id.EditCategory_name)
    EditText CategoryName;

    String categoryType = "Outcome";


    private AppDatabase mDb;



    @BindView(R.id.newAccount_ChosenColor)
    ImageView ChosenColor;


    @BindView(R.id.newAccount_ColorsPlate)
    LinearLayout ColorsPlate;



    int CurrentColorIndex = 0;

    List<RelativeLayout> colors = new ArrayList<>(9);
    List<ImageView> checks = new ArrayList<>(9);


    List<String> ColorsHexa = new ArrayList<>(9);






    @BindView(R.id.AddActivity_FirstLayout)
    LinearLayout AddActivity_FirstLayout;

    @BindView(R.id.AddActivity_SecondLayout)
    LinearLayout AddActivity_SecondLayout;

    String type = "";


    Category categoryObj;
    Boolean Update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        ButterKnife.bind(this);
       // setTimeLineClick(0);
        mDb = AppDatabase.getsInstance(getApplicationContext());
        type = getIntent().getStringExtra("Type");

        if (type.equals("Outcome")){
            Add_OutCome_Text.setText("مصروفات");
        }
        else if (type.equals("Income")){
            Add_OutCome_Text.setText("ايرادات");
        }



        if (getIntent().hasExtra("Category"))
        {
            Update = true;
            categoryObj = (Category) getIntent().getSerializableExtra("Category");
            CategoryName.setText(categoryObj.getName());


            AddActivity_FirstLayout.setBackgroundColor(Color.parseColor(categoryObj.getIcon()));
            AddActivity_SecondLayout.setBackgroundColor(Color.parseColor(categoryObj.getIcon()));




            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                ChosenColor.setBackgroundColor(getResources().getColor(ReturnColor(categoryObj.getIcon())));


            else
                ChosenColor.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), ReturnColor(categoryObj.getIcon())));



        }

        initColors();

    }


/*
    @OnClick(R.id.Add_OutCome)
    void OnOutComeClick() {
        setTimeLineClick(0);
        categoryType = "Outcome";
    }


    @OnClick(R.id.Add_Income)
    void OnInComeClick() {
        setTimeLineClick(1);
        categoryType = "Income";
    }


    void setTimeLineClick(int index) {


        switch (index) {

            case 0:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(NewCategory.this, R.color.TimeLine_Acitve));


                Add_OutCome_Text.setTextColor(getResources().getColor(R.color.white));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(NewCategory.this, R.color.TimeLine_InAcitve));
                Add_Income_Text.setTextColor(getResources().getColor(R.color.white));



                break;


            case 1:


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(NewCategory.this, R.color.TimeLine_InAcitve));


                Add_OutCome_Text.setTextColor(getResources().getColor(R.color.white));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(NewCategory.this, R.color.TimeLine_Acitve));
                Add_Income_Text.setTextColor(getResources().getColor(R.color.white));


                break;


        }


    }

*/


    @OnClick(R.id.NewCategory_Done)
    void addNewCategory(){

        final String categoryName = CategoryName.getText().toString();
        if (TextUtils.isEmpty(categoryName))
        {

        }
        else{


            if (!Update) {

                Sequence seq = mDb.sequenceDao().loadCategorySeq();
                seq.setSeq(seq.getSeq() + 1);

                final Category category = new Category(seq.getSeq(), categoryName, ColorsHexa.get(CurrentColorIndex), type, categoryObj.getDefaultCategory());

                final Sequence seqUpdated = seq;
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.sequenceDao().UpdateCategorySeq(seqUpdated);
                        mDb.categoryDao().insertCategory(category);

                        finish();

                    }
                });

            }
            else if (Update)
            {


                if (!categoryObj.getName().equals(categoryName) || !categoryObj.getIcon().equals(ColorsHexa.get(CurrentColorIndex))){


                    final Category category = new Category(categoryObj.getId(), categoryName, ColorsHexa.get(CurrentColorIndex), type, categoryObj.getDefaultCategory());

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        mDb.categoryDao().UpdateCategory(category);

                        finish();

                    }
                });
            }
finish();
            }
        }

    }




    @OnClick(R.id.NewAccount_cancel)
    void Cancel(){
        finish();
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
