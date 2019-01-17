package com.example.mahmoudbahaa.expenses;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Category;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewCategory extends AppCompatActivity {


    @BindView(R.id.Add_OutCome)
    LinearLayout Add_OutCome;

    @BindView(R.id.Add_Income)
    LinearLayout Add_Income;


    @BindView(R.id.Add_OutCome_Text)
    TextView Add_OutCome_Text;

    @BindView(R.id.Add_Income_Text)
    TextView Add_Income_Text;


    @BindView(R.id.EditCategory_name)
    EditText CategoryName;

    String categoryType = "Outcome";


    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        ButterKnife.bind(this);
        setTimeLineClick(0);
        mDb = AppDatabase.getsInstance(getApplicationContext());

    }



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



    @OnClick(R.id.NewCategory_Done)
    void addNewCategory(){

        final String categoryName = CategoryName.getText().toString();
        if (TextUtils.isEmpty(categoryName))
        {

        }
        else{

            final Category category = new Category(categoryName,"ic_baseline_account_balance_24px",categoryType);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.categoryDao().insertCategory(category);
                    finish();

                }
            });
        }

    }


}
