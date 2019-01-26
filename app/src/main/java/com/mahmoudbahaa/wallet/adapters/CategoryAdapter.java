package com.mahmoudbahaa.wallet.adapters;

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

import com.mahmoudbahaa.wallet.AppExecutors;
import com.mahmoudbahaa.wallet.R;
import com.mahmoudbahaa.wallet.data.AppDatabase;
import com.mahmoudbahaa.wallet.models.Category;

import java.util.Collections;
import java.util.List;

/**
 * Created by MahmoudBahaa on 12/01/2019.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyviewHolder> {

    private final LayoutInflater inflator;
    private Context context;
    final private ListItemClickListener mOnClickListener;
    List<Category> data = Collections.emptyList();




    public interface ListItemClickListener {
        void onCategoryListItemClick(int clickedItemIndex);
    }


    public CategoryAdapter(Context context, List<Category> data, ListItemClickListener listener) {
        inflator =  LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        mOnClickListener = listener;
    }



    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.items_category,parent,false);
        MyviewHolder holder = new MyviewHolder(view);
        return holder;
    }




    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {

        final Category category = data.get(position);


holder.CategoryName.setText(category.getName());

        final int resourceIdOfLastUsedImage = context.getResources().getIdentifier(category.getIcon(), "drawable",
                context.getPackageName());

     //   holder.CategoryIcon.setImageResource(resourceIdOfLastUsedImage);




        if (category.getStatus())
        {
            holder.CategoryStatus.setVisibility(View.VISIBLE);
        }

        else {
            holder.CategoryStatus.setVisibility(View.INVISIBLE);
        }


        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[] {Color.parseColor(category.getIcon()),Color.parseColor(GetColor(category.getIcon()))});
        gd.setCornerRadius(20f);

        holder.Layout.setBackground(gd);




    }





    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public void setCategories(List<Category> categories) {
        this.data = categories;
        notifyDataSetChanged();
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



    class MyviewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

      // ImageView CategoryIcon;
        TextView CategoryName;
        ImageView CategoryStatus ;
        LinearLayout Layout;



        public MyviewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
         //   CategoryIcon = itemView.findViewById(R.id.Category_icon);
            CategoryName = itemView.findViewById(R.id.Category_name);
            CategoryStatus = itemView.findViewById(R.id.Category_Status);
            Layout  = itemView.findViewById(R.id.Account_layOut);


        }


        @Override
        public void onClick(View view) {


            int clickedPosition = getAdapterPosition();
            //   String  CatName = data.get(clickedPosition).getName();
            mOnClickListener.onCategoryListItemClick(clickedPosition);

        }





    }

    public void removeItem(final int position) {

      final  Category DeletedCategory = data.get(position);
        data.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getsInstance(context).categoryDao().deleteCategory(DeletedCategory);
                AppDatabase.getsInstance(context).expenseDao().DeleteAllExpensesWithCategoryId(DeletedCategory.getId());

            }
        });

    }

    public void restoreItem(Category item, int position) {
        data.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public Context getContext(){
        return context;
    }






}
