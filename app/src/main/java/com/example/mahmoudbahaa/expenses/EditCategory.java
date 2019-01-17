package com.example.mahmoudbahaa.expenses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mahmoudbahaa.expenses.adapters.CategoryAdapter;
import com.example.mahmoudbahaa.expenses.adapters.ExpenseAdapter;
import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class EditCategory extends AppCompatActivity implements CategoryAdapter.ListItemClickListener {

    @BindView(R.id.Add_OutCome)
    LinearLayout Add_OutCome;

    @BindView(R.id.Add_Income)
    LinearLayout Add_Income;


    @BindView(R.id.Add_OutCome_Text)
    TextView Add_OutCome_Text;

    @BindView(R.id.Add_Income_Text)
    TextView Add_Income_Text;



    @BindView(R.id.CategoryEdit_OutComeRecycler)
    RecyclerView OutComeRecycler;

    @BindView(R.id.CategoryEdit_IncomeRecycler)
    RecyclerView IncomeRecycler;


    @BindView(R.id.EditCategory_Category)
            LinearLayout CategorySelector;

    CategoryAdapter OutComeAdapter;
    CategoryAdapter IncomeAdapter;






    List<Category> Incomes = new ArrayList<>();
    List<Category> OutComes = new ArrayList<>();


    Boolean Select = false;
    String category = "";

    Category categoryObj;

    private AppDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        setTimeLineClick(0);
        initAdapters();
       // initInsertDummydata();



        if (getIntent().hasExtra("Type")) {
            Select = true;
            category = getIntent().getStringExtra("Type");
            categoryObj = (Category) getIntent().getSerializableExtra("Category");

            initCategoryAsSelect(category);


            switch (category){
            case "Outcome":

                if (categoryObj != null && OutComes.size()>0)
                {
                    int Index =  OutComes.indexOf(categoryObj);
                    OutComes.get(0).setStatus(false);
                    OutComes.get(Index).setStatus(true);
                    OutComeAdapter.notifyDataSetChanged();
                }

                break;

            case "Income":
                if (categoryObj != null && Incomes.size() > 0)
                {
                    int Index =  Incomes.indexOf(categoryObj);
                    Incomes.get(0).setStatus(false);
                    Incomes.get(Index).setStatus(true);

                    IncomeAdapter.notifyDataSetChanged();
                }
                break;

            }



        }
        LoadCategories();

    }


    void initCategoryAsSelect(String Category){

        enableDisableView(findViewById(R.id.EditCategory_Category),false);
        if (Category.equals("Outcome"))
            setTimeLineClick(0);
        else
            setTimeLineClick(1);

    //    findViewById(R.id.EditCategory_AddNew).setVisibility(View.INVISIBLE);


    }


    public  void enableDisableView(View view, boolean enabled) {


        view.setEnabled(enabled);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }


    }

    @OnClick(R.id.Add_OutCome)
    void OnOutComeClick() {
        setTimeLineClick(0);
    }


    @OnClick(R.id.Add_Income)
    void OnInComeClick() {
        setTimeLineClick(1);
    }





    void LoadCategories()
    {

        LiveData<List<Category>> c = mDb.categoryDao().loadIncomesCategories();

        c.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {

                Log.v("categoriesIncome",categories.size()+"");
                Incomes.clear();
                Incomes.addAll(categories);
                IncomeAdapter.notifyDataSetChanged();



                if (categoryObj != null)
                {
                    if (categoryObj.getType().equals("Income")){
                        int Index =  Incomes.indexOf(categoryObj);
                        Incomes.get(0).setStatus(false);

                        Incomes.get(Index).setStatus(true);
                        IncomeAdapter.notifyDataSetChanged();
                    }
                }



            }
        });


        LiveData<List<Category>> a = mDb.categoryDao().loadOutcomesCategories();

        a.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {

                OutComes.clear();
                OutComes.addAll(categories);
                OutComeAdapter.notifyDataSetChanged();

                Log.v("categoriesOutome",categories.size()+"");


                if (categoryObj != null)
                {
                    if (categoryObj.getType().equals("Outcome")){
                    int Index =  OutComes.indexOf(categoryObj);
                        OutComes.get(0).setStatus(false);

                        OutComes.get(Index).setStatus(true);
                    OutComeAdapter.notifyDataSetChanged();
                    }
                }


            }
        });


    }


    void initInsertDummydata(){


        Category e = new Category();

        e.setId(1);
        e.setIcon("ic_baseline_account_balance_24px");
        e.setName("بقالة");
        e.setStatus(false);

        OutComes.add(e);

        e = new Category();
        e.setId(2);
        e.setIcon("ic_baseline_account_balance_24px");
        e.setName("مشتريات");
        e.setStatus(false);

        OutComes.add(e);


        e = new Category();
        e.setId(3);
        e.setIcon("ic_baseline_account_balance_24px");
        e.setName("كهرباء");
        e.setStatus(false);

        OutComes.add(e);


        e = new Category();
        e.setId(1);
        e.setIcon("ic_baseline_account_balance_24px");
        e.setName("مكافأة");
        e.setStatus(false);

        Incomes.add(e);



        e = new Category();
        e.setId(2);
        e.setIcon("ic_baseline_account_balance_24px");
        e.setName("راتب");
        e.setStatus(false);

        Incomes.add(e);


        IncomeAdapter.notifyDataSetChanged();
        OutComeAdapter.notifyDataSetChanged();


    }


    void initAdapters(){



        OutComeAdapter = new CategoryAdapter(getApplicationContext(),OutComes,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        OutComeRecycler.setLayoutManager(layoutManager);
        OutComeRecycler.setAdapter(OutComeAdapter);


        IncomeAdapter = new CategoryAdapter(getApplicationContext(),Incomes,this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        IncomeRecycler.setLayoutManager(layoutManager1);
        IncomeRecycler.setAdapter(IncomeAdapter);
    }


    void setTimeLineClick(int index) {


        switch (index) {

            case 0:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(EditCategory.this, R.color.TimeLine_Acitve));


                Add_OutCome_Text.setTextColor(getResources().getColor(R.color.white));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(EditCategory.this, R.color.TimeLine_InAcitve));
                Add_Income_Text.setTextColor(getResources().getColor(R.color.white));

                OutComeRecycler.setVisibility(View.VISIBLE);
                IncomeRecycler.setVisibility(View.GONE);

                break;


            case 1:


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(EditCategory.this, R.color.TimeLine_InAcitve));


                Add_OutCome_Text.setTextColor(getResources().getColor(R.color.white));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(EditCategory.this, R.color.TimeLine_Acitve));
                Add_Income_Text.setTextColor(getResources().getColor(R.color.white));

                OutComeRecycler.setVisibility(View.GONE);
                IncomeRecycler.setVisibility(View.VISIBLE);

                break;


        }


    }



    @OnClick(R.id.EditCategory_AddNew)
    void AddNewCategory()
    {

        Intent i = new Intent(EditCategory.this,NewCategory.class);

        startActivity(i);


    }




    @Override
    public void onListItemClick(int clickedItemIndex) {


        if (Select)
        {

            Intent data = new Intent();
            if (category.equals("Outcome")) {
                // add data to Intent
                data.putExtra("Category", OutComes.get(clickedItemIndex));


                if (categoryObj!=null){
                    int Index =  OutComes.indexOf(categoryObj);

                    OutComes.get(Index).setStatus(false);
                }
                else
                OutComes.get(0).setStatus(false);

                OutComes.get(clickedItemIndex).setStatus(true);
                OutComeAdapter.notifyDataSetChanged();
            }

            else {

                data.putExtra("Category", Incomes.get(clickedItemIndex));

                if (categoryObj!=null){
                    int Index =  Incomes.indexOf(categoryObj);

                    Incomes.get(Index).setStatus(false);
                }
                else
                    Incomes.get(0).setStatus(false);



                Incomes.get(clickedItemIndex).setStatus(true);

                IncomeAdapter.notifyDataSetChanged();

            }



            setResult(Activity.RESULT_OK, data);

            finish();

        }


    }


    @OnClick(R.id.EditCategory_Back)
    void onBack(){
        finish();
    }

}
