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

import com.example.mahmoudbahaa.expenses.R;
import com.example.mahmoudbahaa.expenses.models.Category;
import com.example.mahmoudbahaa.expenses.models.Expense;

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
        void onListItemClick(int clickedItemIndex);
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

        holder.CategoryIcon.setImageResource(resourceIdOfLastUsedImage);




        if (category.getStatus())
        {
            holder.CategoryStatus.setVisibility(View.VISIBLE);
        }

        else {
            holder.CategoryStatus.setVisibility(View.INVISIBLE);
        }




    }




    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyviewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        ImageView CategoryIcon;
        TextView CategoryName;
        ImageView CategoryStatus ;



        public MyviewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            CategoryIcon = itemView.findViewById(R.id.Category_icon);
            CategoryName = itemView.findViewById(R.id.Category_name);
            CategoryStatus = itemView.findViewById(R.id.Category_Status);


        }


        @Override
        public void onClick(View view) {


            int clickedPosition = getAdapterPosition();
            //   String  CatName = data.get(clickedPosition).getName();
            mOnClickListener.onListItemClick(clickedPosition);

        }





    }






}
