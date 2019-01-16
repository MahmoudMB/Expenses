package com.example.mahmoudbahaa.expenses.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoudbahaa.expenses.models.Expense;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import com.example.mahmoudbahaa.expenses.R;

/**
 * Created by MahmoudBahaa on 11/01/2019.
 */

public class ExpenseAdapter  extends RecyclerView.Adapter<ExpenseAdapter.MyviewHolder> {


    private final LayoutInflater inflator;
    private Context context;
    final private ListItemClickListener mOnClickListener;
    List<Expense> data = Collections.emptyList();


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public ExpenseAdapter(Context context, List<Expense> data, ListItemClickListener listener) {
        inflator =  LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        mOnClickListener = listener;
    }


    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.items_expenses,parent,false);
        MyviewHolder holder = new MyviewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {

        final Expense expense = data.get(position);


        if (expense.getType().equals("Income"))
       // holder.ExpensesStar.setImageResource(ContextCompat.getDrawable(context, R.drawable.ic_baseline_stars_24px));
        {
            ImageViewCompat.setImageTintList(holder.ExpensesStar, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.income)));
            holder.ExpensesPrice.setText(expense.getPrice()+"$");
            holder.ExpensesPrice.setTextColor(ContextCompat.getColor(context,R.color.money_income));

        }
        else {
            ImageViewCompat.setImageTintList(holder.ExpensesStar, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.expense)));
            holder.ExpensesPrice.setText("-"+expense.getPrice()+"$");
            holder.ExpensesPrice.setTextColor(ContextCompat.getColor(context,R.color.money_expense));

        }



        holder.ExpensesAccount.setText(expense.getAccount());
        holder.ExpensesDescription.setText(expense.getDescription());

    }




    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.data = expenses;
        notifyDataSetChanged();
    }


    public List<Expense> getExpenses(){
        return data;
    }

    class MyviewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        ImageView ExpensesStar;
        TextView ExpensesDescription;
        TextView ExpensesAccount;
        TextView ExpensesPrice;



        public MyviewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ExpensesStar = itemView.findViewById(R.id.Expenses_Star);
            ExpensesDescription = itemView.findViewById(R.id.Expenses_Description);
            ExpensesAccount = itemView.findViewById(R.id.Expenses_Account);
            ExpensesPrice = itemView.findViewById(R.id.Expenses_Price);



        }


        @Override
        public void onClick(View view) {


            int clickedPosition = getAdapterPosition();
            //   String  CatName = data.get(clickedPosition).getName();
            mOnClickListener.onListItemClick(clickedPosition);

        }





    }



}
