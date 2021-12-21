package com.daybook.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.databinding.ItemCategoryListBinding;
import com.daybook.db.model.CategoryModel;
import com.daybook.listener.ItemOnClickListener;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {
    List<CategoryModel> categoryList;
    private HomeActivity activity;
    private ItemOnClickListener itemOnClickListener;

    public CategoryListAdapter(List<CategoryModel> categoryList, HomeActivity activity, ItemOnClickListener itemOnClickListener) {
        this.categoryList = categoryList;
        this.activity = activity;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_category_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.dataBinding.tvCategoryName.setText(categoryList.get(position).getCategoryName());
        holder.dataBinding.ivCategoryIcon.setImageResource(activity.getResourceId(categoryList.get(position).getCategoryIcon()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickListener.onItemClicked(position, view, 1);
            }
        });
        if (categoryList.get(position).isSystem()) {
            holder.dataBinding.ivDelete.setVisibility(View.GONE);
        } else {
            holder.dataBinding.ivCategoryIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(categoryList.get(position).getCategoryColor())));
            holder.dataBinding.ivDelete.setVisibility(View.VISIBLE);
            holder.dataBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemOnClickListener.onItemClicked(position, view, 2);
                }
            });
        }
        if (position == categoryList.size() - 1) {
            holder.dataBinding.getRoot().setPadding(0, 0, 0, 100);
        }
        else{
            holder.dataBinding.getRoot().setPadding(0, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryListBinding dataBinding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
