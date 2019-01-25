package com.mahmoudbahaa.expenses.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahmoudbahaa.expenses.AppExecutors;
import com.mahmoudbahaa.expenses.R;
import com.mahmoudbahaa.expenses.data.AppDatabase;
import com.mahmoudbahaa.expenses.models.Account;

import java.util.Collections;
import java.util.List;

/**
 * Created by MahmoudBahaa on 11/01/2019.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyviewHolder>  {


    private final LayoutInflater inflator;
    private Context context;
    final private ListItemClickListener mOnClickListener;
    List<Account> data = Collections.emptyList();


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public AccountAdapter(Context context, List<Account> data, ListItemClickListener listener) {
        inflator =  LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        mOnClickListener = listener;
    }




    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.items_account,parent,false);
        MyviewHolder holder = new MyviewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {

        final Account account = data.get(position);


        holder.AccountName.setText(account.getName());
        final int resourceIdOfLastUsedImage = context.getResources().getIdentifier(account.getIcon(), "drawable",
                context.getPackageName());

//        holder.AccountIcon.setImageResource(resourceIdOfLastUsedImage);


        if (account.getStatus())
        {
            holder.AccountStatus.setVisibility(View.VISIBLE);
        }

        else {
            holder.AccountStatus.setVisibility(View.INVISIBLE);
        }



        holder.AccountTotal.setText(account.getTotal()+"");



        if (account.getTotal()<0)
            holder.AccountTotal.setTextColor(Color.parseColor("#fd284b"));
       // holder.Layout.setBackgroundColor(0xFF616261).setBackgroundColor(Color.parseColor(color));

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[] {Color.parseColor(account.getIcon()),Color.parseColor(GetColor(account.getIcon()))});
        gd.setCornerRadius(20f);

   holder.Layout.setBackground(gd);





        /*
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
        */




    }



    public String GetColor(String c){

       String color = "";

       switch (c)
       {
           case "#8281ff":
               color = "#a398ff";
           break;

           case "#639af4":
               color = "#8fd0fb";
               break;

           case "#7ad2ff":
               color = "#83e1f3";
               break;

           case "#4cd3b2":
               color = "#56e2d2";
               break;

           case "#47d469":
               color = "#9ae481";
               break;

           case "#f2be44":
               color = "#f4dd66";
               break;

           case "#ff965d":
               color = "#fdcc3a";
               break;

           case "#fd7881":
               color = "#fda483";
               break;

           case "#d38cf2":
               color = "#d4a9fe";
               break;

       }


        return  color;
    }


    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }






    public void setAccounts(List<Account> accounts) {
        this.data = accounts;
        notifyDataSetChanged();
    }



    class MyviewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        ImageView AccountIcon;
        TextView AccountName;
        ImageView AccountStatus ;
        TextView AccountTotal;
        LinearLayout Layout;





        public MyviewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
           // AccountIcon = itemView.findViewById(R.id.Account_Icon);
            AccountName = itemView.findViewById(R.id.Account_Name);
            AccountStatus = itemView.findViewById(R.id.Account_Status);
            AccountTotal = itemView.findViewById(R.id.Account_Total);
Layout  = itemView.findViewById(R.id.Account_layOut);
        }


        @Override
        public void onClick(View view) {


            int clickedPosition = getAdapterPosition();
            //   String  CatName = data.get(clickedPosition).getName();
            mOnClickListener.onListItemClick(clickedPosition);

        }





    }


    public void removeItem(final int position) {
        final  Account accountTobeDeleted = data.get(position);

        data.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getsInstance(context).accountDao().deleteAccount(accountTobeDeleted);
                AppDatabase.getsInstance(context).expenseDao().DeleteAllExpensesWithAccountId(accountTobeDeleted.getId());
            }
        });

    }

    public void restoreItem(Account item, int position) {
        data.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public Context getContext(){
        return context;
    }





}
