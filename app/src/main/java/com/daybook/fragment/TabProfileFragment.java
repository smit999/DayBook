package com.daybook.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daybook.R;
import com.daybook.activity.CurrencySelectionActivity;
import com.daybook.activity.HomeActivity;
import com.daybook.adapter.SettingItemListAdapter;
import com.daybook.base.BaseFragment;
import com.daybook.databinding.FragmentProfileTabBinding;
import com.daybook.db.model.SettingItem;
import com.daybook.global.Pref;
import com.daybook.listener.ItemOnClickListener;

import java.util.ArrayList;
import java.util.List;

public class TabProfileFragment extends BaseFragment implements ItemOnClickListener {
    private HomeActivity activity;
    private FragmentProfileTabBinding dataBinding;
    private SettingItemListAdapter adapter;

    List<SettingItem> settingList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_tab, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.actionBar.ivBack.setVisibility(View.GONE);
        dataBinding.actionBar.tvTitle.setText(activity.getStrings(R.string.title_settings));

        int drawableResourceId2 = this.getResources().getIdentifier("ic_notification", "drawable", activity.getPackageName());
        SettingItem settingItem2 = new SettingItem();
        settingItem2.setIconId(drawableResourceId2);
        settingItem2.setSubText("Every Evening Reminder Notification");
        settingItem2.setTitle("Notification");
        settingItem2.setType(2);
        settingList.add(settingItem2);

        int drawableResourceId3 = this.getResources().getIdentifier("ic_categories", "drawable", activity.getPackageName());
        SettingItem settingItem3 = new SettingItem();
        settingItem3.setIconId(drawableResourceId3);
        settingItem3.setSubText("Manage all category");
        settingItem3.setTitle("Categories");
        settingList.add(settingItem3);

        int drawableResourceId4 = this.getResources().getIdentifier("ic_change_currency", "drawable", activity.getPackageName());
        SettingItem settingItem4 = new SettingItem();
        settingItem4.setIconId(drawableResourceId4);
        settingItem4.setTitle("Change Currency");
        settingItem4.setSubText("Change your transaction currency");
        settingList.add(settingItem4);

        int drawableResourceId5 = this.getResources().getIdentifier("ic_rate", "drawable", activity.getPackageName());
        SettingItem settingItem5 = new SettingItem();
        settingItem5.setIconId(drawableResourceId5);
        settingItem5.setSubText("Give your review on playstore");
        settingItem5.setTitle("Rate Us");
        settingList.add(settingItem5);

        adapter = new SettingItemListAdapter(settingList, activity, this);
        dataBinding.rvSettingList.setLayoutManager(new LinearLayoutManager(activity));
        dataBinding.rvSettingList.setItemAnimator(new DefaultItemAnimator());
        dataBinding.rvSettingList.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(int position, View v, int type) {
        if (position == 0) {
            if (Pref.getIsNotificationEnable(activity)) {
                new AlertDialog.Builder(activity).setMessage("Are you sure want stop reminder notification?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Pref.setIsNotificationEnable(activity, false);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Pref.setIsNotificationEnable(activity, true);
                                adapter.notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            } else {
                Pref.setIsNotificationEnable(activity, true);
                adapter.notifyDataSetChanged();
            }
        } else if (position == 1) {
            activity.setFragment(CategoryListFragment.getInstance(3), R.id.mainFrame, true, true, null);
        } else if (position == 2) {
            Intent intent = new Intent(activity, CurrencySelectionActivity.class);
            startActivity(intent);
        } else if (position == 3) {

        }
    }


}
