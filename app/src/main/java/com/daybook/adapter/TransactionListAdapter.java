package com.daybook.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.customview.ViewBinderHelper;
import com.daybook.databinding.ItemTransactionListBinding;
import com.daybook.db.model.TransactionModel;
import com.daybook.global.Pref;
import com.daybook.listener.ItemOnClickListener;

import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.MyViewHolder> {
    private List<TransactionModel> transactionList;
    private HomeActivity activity;
    private ItemOnClickListener itemOnClickListener;
    private int callFrom;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();


    public TransactionListAdapter(List<TransactionModel> transactionList, HomeActivity activity, ItemOnClickListener itemOnClickListener, int callFrom) {
        this.transactionList = transactionList;
        this.activity = activity;
        this.itemOnClickListener = itemOnClickListener;
        this.callFrom = callFrom;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_transaction_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        TransactionModel transactionModel = transactionList.get(position);

        holder.dataBinding.tvSubject.setText(transactionModel.getSubject());
        holder.dataBinding.tvDate.setText(activity.getDateFormatRange(transactionModel.getTimeStamp()));
        binderHelper.bind(holder.dataBinding.swipeLayout, transactionList.get(position).getTransactionId() + "");
        binderHelper.setOpenOnlyOne(true);
        if ((transactionModel.getTransactionInfo() == null || transactionModel.getTransactionInfo().equals(""))
                && (transactionModel.getAttachmentImage() == null || transactionModel.getAttachmentImage().equals(""))) {
            holder.dataBinding.ivInfo.setVisibility(View.GONE);
        } else {
            holder.dataBinding.ivInfo.setVisibility(View.VISIBLE);
        }


        if (transactionModel.getTransactionType() == 1) {
            holder.dataBinding.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.red));
            holder.dataBinding.tvAmount.setText("-" + Pref.getCurrencySymbol(activity) + transactionModel.getAmount());
        } else {
            holder.dataBinding.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.green));
            holder.dataBinding.tvAmount.setText("+" + Pref.getCurrencySymbol(activity) + transactionModel.getAmount());
        }
        holder.dataBinding.ivCategoryIcon.setImageResource(activity.getResourceId(transactionList.get(position).getCategoryIcon()));
        holder.dataBinding.ivCategoryIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(transactionModel.getCategoryColor())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickListener.onItemClicked(position, view, 1);
            }
        });

        holder.dataBinding.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickListener.onItemClicked(position, view, 4);
                binderHelper.closeLayout(transactionModel.getTransactionId() + "");
            }
        });
        holder.dataBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickListener.onItemClicked(position, view, 2);
                binderHelper.closeLayout(transactionModel.getTransactionId() + "");

            }
        });
        holder.dataBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickListener.onItemClicked(position, view, 3);
                binderHelper.closeLayout(transactionModel.getTransactionId() + "");

            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemTransactionListBinding dataBinding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
