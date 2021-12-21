package com.daybook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.databinding.ItemSettingListBinding;
import com.daybook.db.model.SettingItem;
import com.daybook.global.Pref;
import com.daybook.listener.ItemOnClickListener;

import java.util.List;

public class SettingItemListAdapter extends RecyclerView.Adapter<SettingItemListAdapter.MyViewHolder> {
    private List<SettingItem> settingItemList;
    private HomeActivity activity;
    private ItemOnClickListener itemOnClickListener;

    public SettingItemListAdapter(List<SettingItem> settingItemList, HomeActivity activity, ItemOnClickListener itemOnClickListener) {
        this.settingItemList = settingItemList;
        this.activity = activity;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_setting_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (settingItemList.get(position).getType() == 2) {
            holder.dataBiding.switchNotification.setVisibility(View.VISIBLE);
            holder.dataBiding.ivNextArrow.setVisibility(View.GONE);
            holder.dataBiding.switchNotification.setChecked(Pref.getIsNotificationEnable(activity));
            holder.dataBiding.switchNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemOnClickListener.onItemClicked(position,view,settingItemList.get(position).getType());
                }
            });
        } else {
            holder.dataBiding.ivNextArrow.setVisibility(View.VISIBLE);
            holder.dataBiding.switchNotification.setVisibility(View.GONE);
        }
        holder.dataBiding.ivIcon.setImageResource(settingItemList.get(position).getIconId());
        holder.dataBiding.tvSubText.setText(settingItemList.get(position).getSubText());
        holder.dataBiding.tvTitle.setText(settingItemList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickListener.onItemClicked(position, view, settingItemList.get(position).getType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSettingListBinding dataBiding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dataBiding = DataBindingUtil.bind(itemView);
        }
    }
}
