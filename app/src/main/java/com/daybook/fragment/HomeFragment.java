package com.daybook.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.adapter.TabAdapter;
import com.daybook.base.BaseFragment;
import com.daybook.databinding.FragmentHomeBinding;
import com.daybook.db.DayBookDataBase;
import com.daybook.db.model.CategoryModel;
import com.daybook.global.Constants;
import com.daybook.global.Pref;
import com.daybook.utils.Logger;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private FragmentHomeBinding dataBinding;
    private HomeActivity activity;
    private TabAdapter adapter;
    private boolean isTabChangeByDashboard = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
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

        if (Pref.getIsFirstTime(activity)) {
            setUpDefaultDatabase();
        }

        adapter = new TabAdapter(getChildFragmentManager(), activity.getLifecycle());
        adapter.addFragment(new TabDashboardFragment(), activity.getStrings(R.string.title_dashboard));
        adapter.addFragment(TabTransactionFragment.getInstance(1, 0, 0, 0, "", ""), activity.getStrings(R.string.title_active));
        adapter.addFragment(new TabProfileFragment(), activity.getStrings(R.string.title_completed));
        dataBinding.viewPager.setAdapter(adapter);
        dataBinding.viewPager.setOffscreenPageLimit(3);


        new TabLayoutMediator(dataBinding.tabLayout, dataBinding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Logger.d("OnTab", position + "");

            }
        }).attach();
        setTabInitialize();


        dataBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Logger.d("TabSelected", tab.getPosition() + "");
                setTabSelection(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpDefaultDatabase() {

        final CategoryModel categoryModel = new CategoryModel();
        categoryModel.setCategoryColor("#ea1e63");
        categoryModel.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel.setCreated(System.currentTimeMillis() / 1000);
        categoryModel.setCategoryType(1);
        categoryModel.setCategoryName("Food");
        categoryModel.setCategoryIcon("food");
        categoryModel.setSystem(true);

        CategoryModel categoryModel2 = new CategoryModel();
        categoryModel2.setCategoryColor("#f64136");
        categoryModel2.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel2.setCreated(System.currentTimeMillis() / 1000);
        categoryModel2.setCategoryType(1);
        categoryModel2.setCategoryName("Education");
        categoryModel2.setCategoryIcon("education");
        categoryModel2.setSystem(true);

        CategoryModel categoryModel3 = new CategoryModel();
        categoryModel3.setCategoryColor("#2096f0");
        categoryModel3.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel3.setCreated(System.currentTimeMillis() / 1000);
        categoryModel3.setCategoryType(1);
        categoryModel3.setCategoryName("EMI");
        categoryModel3.setCategoryIcon("emi");
        categoryModel3.setSystem(true);

        CategoryModel categoryModel4 = new CategoryModel();
        categoryModel4.setCategoryColor("#9c28b1");
        categoryModel4.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel4.setCreated(System.currentTimeMillis() / 1000);
        categoryModel4.setCategoryType(1);
        categoryModel4.setCategoryName("Health");
        categoryModel4.setCategoryIcon("health");
        categoryModel4.setSystem(true);

        CategoryModel categoryModel5 = new CategoryModel();
        categoryModel5.setCategoryColor("#683cb8");
        categoryModel5.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel5.setCreated(System.currentTimeMillis() / 1000);
        categoryModel5.setCategoryType(1);
        categoryModel5.setCategoryName("Shopping");
        categoryModel5.setCategoryIcon("shopping");
        categoryModel5.setSystem(true);

        CategoryModel categoryModel6 = new CategoryModel();
        categoryModel6.setCategoryColor("#00bcd2");
        categoryModel6.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel6.setCreated(System.currentTimeMillis() / 1000);
        categoryModel6.setCategoryType(1);
        categoryModel6.setCategoryName("Transportation");
        categoryModel6.setCategoryIcon("transportation");
        categoryModel6.setSystem(true);


        CategoryModel categoryModel8 = new CategoryModel();
        categoryModel8.setCategoryColor("#3f51b3");
        categoryModel8.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel8.setCreated(System.currentTimeMillis() / 1000);
        categoryModel8.setCategoryType(1);
        categoryModel8.setCategoryName("Entertainment");
        categoryModel8.setCategoryIcon("entertainment");
        categoryModel8.setSystem(true);

        CategoryModel categoryModel7 = new CategoryModel();
        categoryModel7.setCategoryColor("#fe5722");
        categoryModel7.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel7.setCreated(System.currentTimeMillis() / 1000);
        categoryModel7.setCategoryType(1);
        categoryModel7.setCategoryName("Other");
        categoryModel7.setCategoryIcon("other");
        categoryModel7.setSystem(true);

        //Income Category
        CategoryModel categoryModel9 = new CategoryModel();
        categoryModel9.setCategoryColor("#009788");
        categoryModel9.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel9.setCreated(System.currentTimeMillis() / 1000);
        categoryModel9.setCategoryType(2);
        categoryModel9.setCategoryName("Invoice payment");
        categoryModel9.setCategoryIcon("invoice");
        categoryModel9.setSystem(true);

        CategoryModel categoryModel10 = new CategoryModel();
        categoryModel10.setCategoryColor("#4cb04e");
        categoryModel10.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel10.setCreated(System.currentTimeMillis() / 1000);
        categoryModel10.setCategoryType(2);
        categoryModel10.setCategoryName("Salary");
        categoryModel10.setCategoryIcon("salary");
        categoryModel10.setSystem(true);

        CategoryModel categoryModel11 = new CategoryModel();
        categoryModel11.setCategoryColor("#8cc24a");
        categoryModel11.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel11.setCreated(System.currentTimeMillis() / 1000);
        categoryModel11.setCategoryType(2);
        categoryModel11.setCategoryName("Loan");
        categoryModel11.setCategoryIcon("loan");
        categoryModel11.setSystem(true);

        CategoryModel categoryModel12 = new CategoryModel();
        categoryModel12.setCategoryColor("#bfca30");
        categoryModel12.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel12.setCreated(System.currentTimeMillis() / 1000);
        categoryModel12.setCategoryType(2);
        categoryModel12.setCategoryName("Gift");
        categoryModel12.setCategoryIcon("gift");
        categoryModel12.setSystem(true);

        CategoryModel categoryModel13 = new CategoryModel();
        categoryModel13.setCategoryColor("#fac02e");
        categoryModel13.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel13.setCreated(System.currentTimeMillis() / 1000);
        categoryModel13.setCategoryType(2);
        categoryModel13.setCategoryName("Cash");
        categoryModel13.setCategoryIcon("cash");
        categoryModel13.setSystem(true);

        CategoryModel categoryModel14 = new CategoryModel();
        categoryModel14.setCategoryColor("#fe9700");
        categoryModel14.setUpdated(System.currentTimeMillis() / 1000);
        categoryModel14.setCreated(System.currentTimeMillis() / 1000);
        categoryModel14.setCategoryType(2);
        categoryModel14.setCategoryName("Cheque");
        categoryModel14.setCategoryIcon("cheque");
        categoryModel14.setSystem(true);

        final List<CategoryModel> categoryModels = new ArrayList<>();
        categoryModels.add(categoryModel);
        categoryModels.add(categoryModel2);
        categoryModels.add(categoryModel3);
        categoryModels.add(categoryModel4);
        categoryModels.add(categoryModel5);
        categoryModels.add(categoryModel6);
        categoryModels.add(categoryModel8);
        categoryModels.add(categoryModel7);
        categoryModels.add(categoryModel9);
        categoryModels.add(categoryModel10);
        categoryModels.add(categoryModel11);
        categoryModels.add(categoryModel12);
        categoryModels.add(categoryModel13);
        categoryModels.add(categoryModel14);


        DayBookDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DayBookDataBase.getDatabase(activity).categoryDao().insertAllCategory(categoryModels);
                Logger.d("Insert Completed");
                Pref.setIsFirstTime(activity, false);
            }
        });


    }

    private void setTabInitialize() {
        dataBinding.tabLayout.getTabAt(0).setCustomView(R.layout.tab_item_view);
        dataBinding.tabLayout.getTabAt(1).setCustomView(R.layout.tab_item_view);
        dataBinding.tabLayout.getTabAt(2).setCustomView(R.layout.tab_item_view);
        setTabSelection(0);

    }

    private void setTabSelection(int position) {
        switch (position) {
            case 0: {
                View tab1 = dataBinding.tabLayout.getTabAt(0).getCustomView();
                ImageView iv = tab1.findViewById(R.id.ivTabIcon);
                iv.setImageResource(R.drawable.home_tab);
                TextView tv = tab1.findViewById(R.id.tvTabTitle);
                tv.setTextColor(ContextCompat.getColor(activity, R.color.orange_select));
                tv.setText("Home");

                View tab2 = dataBinding.tabLayout.getTabAt(1).getCustomView();
                ImageView iv2 = tab2.findViewById(R.id.ivTabIcon);
                iv2.setImageResource(R.drawable.transaction_tab);
                TextView tv2 = tab2.findViewById(R.id.tvTabTitle);
                tv2.setTextColor(ContextCompat.getColor(activity, R.color.black));
                tv2.setText("Transaction");

                View tab3 = dataBinding.tabLayout.getTabAt(2).getCustomView();
                ImageView iv3 = tab3.findViewById(R.id.ivTabIcon);
                iv3.setImageResource(R.drawable.settings_tab);
                TextView tv3 = tab3.findViewById(R.id.tvTabTitle);
                tv3.setTextColor(ContextCompat.getColor(activity, R.color.black));
                tv3.setText("Settings");

            }
            break;
            case 1: {
                View tab1 = dataBinding.tabLayout.getTabAt(0).getCustomView();
                ImageView iv = tab1.findViewById(R.id.ivTabIcon);
                iv.setImageResource(R.drawable.home_tab_unselect);
                TextView tv = tab1.findViewById(R.id.tvTabTitle);
                tv.setTextColor(ContextCompat.getColor(activity, R.color.black));

                View tab2 = dataBinding.tabLayout.getTabAt(1).getCustomView();
                ImageView iv2 = tab2.findViewById(R.id.ivTabIcon);
                iv2.setImageResource(R.drawable.transaction_tab_select);
                TextView tv2 = tab2.findViewById(R.id.tvTabTitle);
                tv2.setTextColor(ContextCompat.getColor(activity, R.color.orange_select));

                View tab3 = dataBinding.tabLayout.getTabAt(2).getCustomView();
                ImageView iv3 = tab3.findViewById(R.id.ivTabIcon);
                iv3.setImageResource(R.drawable.settings_tab);
                TextView tv3 = tab3.findViewById(R.id.tvTabTitle);
                tv3.setTextColor(ContextCompat.getColor(activity, R.color.black));

                if (isTabChangeByDashboard) {
                    isTabChangeByDashboard = false;
                } else {
                    adapter.getFragmentAt(1).onActivityResult(Constants.TAB_CHANGE_REQ, 200, null);

                }
                break;
            }
            case 2: {
                View tab1 = dataBinding.tabLayout.getTabAt(0).getCustomView();
                ImageView iv = tab1.findViewById(R.id.ivTabIcon);
                iv.setImageResource(R.drawable.home_tab_unselect);
                TextView tv = tab1.findViewById(R.id.tvTabTitle);
                tv.setTextColor(ContextCompat.getColor(activity, R.color.black));

                View tab2 = dataBinding.tabLayout.getTabAt(1).getCustomView();
                ImageView iv2 = tab2.findViewById(R.id.ivTabIcon);
                iv2.setImageResource(R.drawable.transaction_tab);
                TextView tv2 = tab2.findViewById(R.id.tvTabTitle);
                tv2.setTextColor(ContextCompat.getColor(activity, R.color.black));

                View tab3 = dataBinding.tabLayout.getTabAt(2).getCustomView();
                ImageView iv3 = tab3.findViewById(R.id.ivTabIcon);
                iv3.setImageResource(R.drawable.settings_tab_select);
                TextView tv3 = tab3.findViewById(R.id.tvTabTitle);
                tv3.setTextColor(ContextCompat.getColor(activity, R.color.orange_select));
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult Home");
        if (requestCode == Constants.ADD_TRANSACTION_REQ_CODE && resultCode == Constants.ADD_TRANSACTION_RESULT_CODE) {
            Logger.d("onActivityResult Home IF");
            adapter.getFragmentAt(0).onActivityResult(requestCode, resultCode, data);
            adapter.getFragmentAt(1).onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == Constants.BACK_PRESS_REQ) {
            if (dataBinding.viewPager.getCurrentItem() != 0)
                dataBinding.viewPager.setCurrentItem(0);
            else {
                activity.finish();
            }
        }
    }
    public void setTransactionTab(int i, long startDate, long endDate, int category_id) {
        isTabChangeByDashboard = true;
        dataBinding.viewPager.setCurrentItem(1);
        Intent intent = new Intent();
        intent.putExtra("CALL_FROM", i);
        intent.putExtra("START_DATE", startDate);
        intent.putExtra("END_DATE", endDate);
        intent.putExtra("CATEGORY_ID", category_id);
        adapter.getFragmentAt(1).onActivityResult(Constants.DASHBOARD_TRANSACTION_REQ, 200, intent);
    }
}