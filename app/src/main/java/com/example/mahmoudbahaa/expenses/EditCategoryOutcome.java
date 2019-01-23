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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.mahmoudbahaa.expenses.adapters.CategoryAdapter;
import com.example.mahmoudbahaa.expenses.adapters.CategorySwipeToDeleteCallback;
import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCategoryOutcome extends AppCompatActivity  implements CategoryAdapter.ListItemClickListener{


    @BindView(R.id.CategoryEdit_OutComeRecycler)
    RecyclerView OutComeRecycler;


    CategoryAdapter OutComeAdapter;

    List<Category> OutComes = new ArrayList<>();


    Boolean Select = false;
    String category = "";

    Category categoryObj;
    String ScreenType = "";



    Boolean FirstRun = true;


    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category_outcome);

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


                    if (categoryObj != null && OutComes.size()>0)
                    {
                        int Index =  OutComes.indexOf(categoryObj);
                        OutComes.get(0).setStatus(false);
                        OutComes.get(Index).setStatus(true);
                        OutComeAdapter.notifyDataSetChanged();
                    }




        }
        LoadCategories();

    }


    void LoadCategories()
    {


        LiveData<List<Category>> a = mDb.categoryDao().loadOutcomesCategories();

        a.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {

                OutComes.clear();
                OutComes.addAll(categories);
                OutComeAdapter.notifyDataSetChanged();

                Log.v("categoriesOutome",categories.size()+"");


                if (categoryObj != null && FirstRun)
                {
                    if (categoryObj.getType().equals("Outcome")){
                        int Index =  OutComes.indexOf(categoryObj);
                        OutComes.get(Index).setStatus(true);
                        OutComeAdapter.notifyDataSetChanged();
                    }
                }

            }
        });


    }


    void initAdapters(){



        OutComeAdapter = new CategoryAdapter(getApplicationContext(),OutComes,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        OutComeRecycler.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new CategorySwipeToDeleteCallback(OutComeAdapter));
        itemTouchHelper.attachToRecyclerView(OutComeRecycler);
        OutComeRecycler.setAdapter(OutComeAdapter);


        OutComeRecycler.setAdapter(OutComeAdapter);


    }


    @OnClick(R.id.EditCategory_AddNew)
    void AddNewCategory()
    {

        Intent i = new Intent(EditCategoryOutcome.this,NewCategory.class);
        i.putExtra("Type",category);

        startActivity(i);


    }


    @Override
    public void onCategoryListItemClick(int clickedItemIndex) {


        FirstRun = false;


        if (Select) {

            switch (ScreenType) {

                case "General":

                    if (category.equals("Outcome")) {
                        if (!categoryObj.equals(OutComes.get(clickedItemIndex))) {


                            int index = OutComes.indexOf(categoryObj);
                            OutComes.get(index).setDefaultCategory(false);
                            OutComes.get(clickedItemIndex).setDefaultCategory(true);

                            OutComes.get(index).setStatus(false);
                            OutComes.get(clickedItemIndex).setStatus(true);

                            OutComeAdapter.notifyDataSetChanged();
                            changeCategory(OutComes.get(index));
                            changeCategory(OutComes.get(clickedItemIndex));
                            finish();
                        }
                        finish();

                    }

                    break;

                case "Add":
                    Intent data = new Intent();

                    if (category.equals("Outcome")) {
                        Log.v("category1",category);

                        if (!categoryObj.equals(OutComes.get(clickedItemIndex))) {


                            int index = OutComes.indexOf(categoryObj);
                            OutComes.get(index).setStatus(false);
                            OutComes.get(clickedItemIndex).setStatus(true);
                            OutComeAdapter.notifyDataSetChanged();

                        }
                        data.putExtra("Category", OutComes.get(clickedItemIndex));

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
