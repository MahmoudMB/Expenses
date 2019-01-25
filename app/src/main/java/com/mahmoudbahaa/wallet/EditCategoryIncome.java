package com.mahmoudbahaa.expenses;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.mahmoudbahaa.expenses.adapters.CategoryAdapter;
import com.mahmoudbahaa.expenses.adapters.CategorySwipeToDeleteCallback;
import com.mahmoudbahaa.expenses.data.AppDatabase;
import com.mahmoudbahaa.expenses.models.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCategoryIncome extends AppCompatActivity implements CategoryAdapter.ListItemClickListener {


    @BindView(R.id.CategoryEdit_IncomeRecycler)
    RecyclerView IncomeRecycler;
    CategoryAdapter IncomeAdapter;
    List<Category> Incomes = new ArrayList<>();

    Boolean Select = false;
    String category = "";

    Category categoryObj;
    String ScreenType = "";



    Boolean FirstRun = true;


    private AppDatabase mDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category_income);
        ButterKnife.bind(this);

        FirstRun = true;

        if (savedInstanceState!=null)
            FirstRun =    savedInstanceState.getBoolean("FirstRun");


        mDb = AppDatabase.getsInstance(getApplicationContext());
        initAdapters();


        if (getIntent().hasExtra("Type")) {
            Select = true;
            category = getIntent().getStringExtra("Type");
            ScreenType = getIntent().getStringExtra("ScreenType");
            categoryObj = (Category) getIntent().getSerializableExtra("Category");


                    if (categoryObj != null && Incomes.size() > 0)
                    {
                        int Index =  Incomes.indexOf(categoryObj);
                        Incomes.get(0).setStatus(false);
                        Incomes.get(Index).setStatus(true);

                        IncomeAdapter.notifyDataSetChanged();
                    }

        }
        LoadCategories();

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
                    if (categoryObj.getType().equals("Income") && FirstRun){

                        int Index =  Incomes.indexOf(categoryObj);
                        Incomes.get(Index).setStatus(true);

                        IncomeAdapter.notifyDataSetChanged();
                    }
                }



            }
        });



    }



    void initAdapters(){


        IncomeAdapter = new CategoryAdapter(getApplicationContext(),Incomes,this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        IncomeRecycler.setLayoutManager(layoutManager1);


        ItemTouchHelper itemTouchHelper1 = new
                ItemTouchHelper(new CategorySwipeToDeleteCallback(IncomeAdapter));
        itemTouchHelper1.attachToRecyclerView(IncomeRecycler);



        IncomeRecycler.setAdapter(IncomeAdapter);


    }



    @OnClick(R.id.EditCategory_AddNew)
    void AddNewCategory()
    {

        Intent i = new Intent(EditCategoryIncome.this,NewCategory.class);
        i.putExtra("Type",category);

        startActivity(i);


    }



    @Override
    public void onCategoryListItemClick(int clickedItemIndex) {


        FirstRun = false;


        if (Select) {

            switch (ScreenType) {

                case "General":

                 if (category.equals("Income")) {

                        if (!categoryObj.equals(Incomes.get(clickedItemIndex))) {
                            int index = Incomes.indexOf(categoryObj);
                            Incomes.get(index).setDefaultCategory(false);
                            Incomes.get(clickedItemIndex).setDefaultCategory(true);


                            Incomes.get(index).setStatus(false);
                            Incomes.get(clickedItemIndex).setStatus(true);


                            IncomeAdapter.notifyDataSetChanged();

                            changeCategory(Incomes.get(index));
                            changeCategory(Incomes.get(clickedItemIndex));

                            finish();
                        }
                        finish();
                    }


                    break;

                case "Add":
                    Intent data = new Intent();

                    Log.v("Add","1");

                 if (category.equals("Income")) {

                        if (!categoryObj.equals(Incomes.get(clickedItemIndex))) {


                            int index = Incomes.indexOf(categoryObj);
                            Incomes.get(index).setStatus(false);
                            Incomes.get(clickedItemIndex).setStatus(true);
                            IncomeAdapter.notifyDataSetChanged();
                        }
                       Log.v("Add1",Incomes.get(clickedItemIndex).getName()+"");
                        data.putExtra("Category", Incomes.get(clickedItemIndex));

                        setResult(Activity.RESULT_OK, data);
                        finish();
                    }


                    break;


            }


        }
    }



    void changeCategory(final Category c){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.categoryDao().UpdateCategory(c);

            }
        });

    }




    @OnClick(R.id.EditCategory_Back)
    void onBack(){
        finish();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("FirstRun",FirstRun);
    }

}
