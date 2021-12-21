package com.daybook.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.databinding.ItemChartCategoryBinding;
import com.daybook.db.model.CategoryModel;

import java.util.List;

public class ChartCategoryListAdapter extends RecyclerView.Adapter<ChartCategoryListAdapter.MyViewHolder> {
    private List<CategoryModel> categoryList;
    private HomeActivity activity;

    public ChartCategoryListAdapter(List<CategoryModel> categoryList, HomeActivity activity) {
        this.categoryList = categoryList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_chart_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.dataBinding.tvCategoryName.setText(categoryList.get(position).getCategoryName());
        holder.dataBinding.view.setBackgroundColor(Color.parseColor(categoryList.get(position).getCategoryColor()));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemChartCategoryBinding dataBinding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
