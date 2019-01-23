package com.example.mahmoudbahaa.expenses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mahmoudbahaa.expenses.adapters.AccountSwipeToDeleteCallback;
import com.example.mahmoudbahaa.expenses.adapters.CategoryAdapter;
import com.example.mahmoudbahaa.expenses.adapters.CategorySwipeToDeleteCallback;
import com.example.mahmoudbahaa.expenses.data.AppDatabase;
import com.example.mahmoudbahaa.expenses.models.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MahmoudBahaa on 21/01/2019.
 */

public class EditCategories  extends AppCompatActivity implements CategoryAdapter.ListItemClickListener {

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



    String category = "Outcome";

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        setTimeLineClick(0);
        initAdapters();

        LoadCategories();



    }


    @OnClick(R.id.Add_OutCome)
    void OnOutComeClick() {
        setTimeLineClick(0);
        category = "Outcome";
    }


    @OnClick(R.id.Add_Income)
    void OnInComeClick() {
        setTimeLineClick(1);
        category = "Income";
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


        IncomeAdapter = new CategoryAdapter(getApplicationContext(),Incomes,this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        IncomeRecycler.setLayoutManager(layoutManager1);


        ItemTouchHelper itemTouchHelper1 = new
                ItemTouchHelper(new CategorySwipeToDeleteCallback(IncomeAdapter));
        itemTouchHelper1.attachToRecyclerView(IncomeRecycler);


        IncomeRecycler.setAdapter(IncomeAdapter);
    }


    void setTimeLineClick(int index) {


        switch (index) {

            case 0:

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(EditCategories.this, R.color.TimeLine_Acitve));


                Add_OutCome_Text.setTextColor(getResources().getColor(R.color.white));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(EditCategories.this, R.color.TimeLine_InAcitve));
                Add_Income_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));

                OutComeRecycler.setVisibility(View.VISIBLE);
                IncomeRecycler.setVisibility(View.GONE);

                break;


            case 1:


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_OutCome.setBackgroundColor(getResources().getColor(R.color.TimeLine_InAcitve));

                else
                    Add_OutCome.setBackgroundTintList(ContextCompat.getColorStateList(EditCategories.this, R.color.TimeLine_InAcitve));


                Add_OutCome_Text.setTextColor(getResources().getColor(R.color.TimeLineInActive));


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
                    Add_Income.setBackgroundColor(getResources().getColor(R.color.TimeLine_Acitve));

                else
                    Add_Income.setBackgroundTintList(ContextCompat.getColorStateList(EditCategories.this, R.color.TimeLine_Acitve));
                Add_Income_Text.setTextColor(getResources().getColor(R.color.white));

                OutComeRecycler.setVisibility(View.GONE);
                IncomeRecycler.setVisibility(View.VISIBLE);

                break;


        }


    }


    @Override
    public void onCategoryListItemClick(int clickedItemIndex) {

        Intent i = new Intent(EditCategories.this,NewCategory.class);
        i.putExtra("Type",category);
        if (category.equals("Income"))
        i.putExtra("Category",Incomes.get(clickedItemIndex));

        if (category.equals("Outcome"))
            i.putExtra("Category",OutComes.get(clickedItemIndex));


        startActivity(i);




    }

    @OnClick(R.id.EditCategory_Back)
    void onBack(){
        finish();
    }



    @OnClick(R.id.EditCategory_AddNew)
    void AddNewCategory()
    {

        Intent i = new Intent(EditCategories.this,NewCategory.class);
        i.putExtra("Type",category);

        startActivity(i);



    }


}
