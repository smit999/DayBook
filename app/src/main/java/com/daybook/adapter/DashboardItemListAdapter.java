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
import com.daybook.databinding.ItemDashboardListBinding;
import com.daybook.databinding.ItemTransactionListBinding;
import com.daybook.db.model.DashboardItem;
import com.daybook.global.Pref;
import com.daybook.listener.ItemOnClickListener;

import java.util.List;

public class DashboardItemListAdapter extends RecyclerView.Adapter<DashboardItemListAdapter.MyViewHolder> {
    private List<DashboardItem> transactionList;
    private HomeActivity activity;
    private ItemOnClickListener itemOnClickListener;
    private int callFrom;

    public DashboardItemListAdapter(List<DashboardItem> transactionList, HomeActivity activity, ItemOnClickListener itemOnClickListener, int callFrom) {
        this.transactionList = transactionList;
        this.activity = activity;
        this.itemOnClickListener = itemOnClickListener;
        this.callFrom = callFrom;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_dashboard_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        DashboardItem transactionModel = transactionList.get(position);
        holder.dataBinding.tvCategoryName.setText(transactionModel.getCategory_name());
        holder.dataBinding.tvTransactionCount.setText(transactionModel.getTransaction_count() + " Transaction");
        holder.dataBinding.ivCategoryIcon.setImageResource(activity.getResourceId(transactionList.get(position).getCategory_icon()));
        holder.dataBinding.ivCategoryIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(transactionModel.getCategory_color())));
        holder.dataBinding.tvAmount.setText(Pref.getCurrencySymbol(activity) + transactionModel.getTotal());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickListener.onItemClicked(position, view, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemDashboardListBinding dataBinding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
