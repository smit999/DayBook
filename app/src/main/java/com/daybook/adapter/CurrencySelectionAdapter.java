package com.daybook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daybook.R;
import com.daybook.activity.CurrencySelectionActivity;
import com.daybook.base.BaseActivity;
import com.daybook.databinding.ItemCurrencySelectionBinding;
import com.daybook.db.model.CurrencyModel;
import com.daybook.listener.ItemOnClickListener;

import java.util.List;

public class CurrencySelectionAdapter extends RecyclerView.Adapter<CurrencySelectionAdapter.MyViewHolder> {
    private BaseActivity context;
    private List<CurrencyModel> currencyList;
    private ItemOnClickListener itemOnClickListener;

    public CurrencySelectionAdapter(BaseActivity context, List<CurrencyModel> currencyList, ItemOnClickListener itemOnClickListener) {
        this.context = context;
        this.currencyList = currencyList;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_currency_selection, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.dataBinding.tvCurrencyName.setText(currencyList.get(position).getSort_name() + "  " + currencyList.get(position).getSymbol());
        holder.dataBinding.ivFlag.setImageResource(context.getResourceId(currencyList.get(position).getFlag()));
        holder.dataBinding.rbRadio.setChecked(currencyList.get(position).isSelected());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    itemOnClickListener.onItemClicked(position, view, 1);
            }
        });
        holder.dataBinding.rbRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickListener.onItemClicked(position, view, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCurrencySelectionBinding dataBinding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }

    }
}
