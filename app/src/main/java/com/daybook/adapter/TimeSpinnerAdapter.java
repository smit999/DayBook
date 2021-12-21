package com.daybook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.listener.ItemOnClickListener;

import java.util.ArrayList;
import java.util.List;

public class TimeSpinnerAdapter extends BaseAdapter {
    private HomeActivity activity;
    private List<String> days;
    private ItemOnClickListener itemOnClickListener;
    private int callFrom;

    public TimeSpinnerAdapter(HomeActivity activity, List<String> days, ItemOnClickListener itemOnClickListener, int callFrom) {
        this.activity = activity;
        this.days = days;
        this.itemOnClickListener = itemOnClickListener;
        this.callFrom = callFrom;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int i) {
        return days.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final View view1 = LayoutInflater.from(activity).inflate(R.layout.item_single_list, null);
        TextView tv = view1.findViewById(R.id.tvItemName);
        tv.setText(days.get(i));
        return view1;
    }
}
