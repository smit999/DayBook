package com.daybook.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.adapter.ChartCategoryListAdapter;
import com.daybook.adapter.DashboardItemListAdapter;
import com.daybook.adapter.TimeSpinnerAdapter;
import com.daybook.adapter.TransactionListAdapter;
import com.daybook.base.BaseFragment;
import com.daybook.databinding.FragmentDashboardTabBinding;
import com.daybook.db.DayBookDataBase;
import com.daybook.db.model.CategoryModel;
import com.daybook.db.model.DashboardItem;
import com.daybook.db.model.TransactionModel;
import com.daybook.global.Constants;
import com.daybook.global.Pref;
import com.daybook.listener.ItemOnClickListener;
import com.daybook.utils.Logger;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TabDashboardFragment extends BaseFragment implements OnChartValueSelectedListener, ItemOnClickListener, View.OnClickListener {
    private HomeActivity activity;
    private FragmentDashboardTabBinding dataBinding;
    private List<DashboardItem> transactionList = new ArrayList<>();
    private long startDate = 0;
    private long endDate = System.currentTimeMillis() / 1000;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_tab, container, false);
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
        dataBinding.actionBar.tvTitle.setText(activity.getStrings(R.string.title_dashboard));
        getDashBoardData();

        List<String> days = Arrays.asList("This Week", "This Month", "This Year", "Life Time", "Custom");
        /*
        TimeSpinnerAdapter mAdapterSpinner = new TimeSpinnerAdapter(activity, days, TabDashboardFragment.this, 2) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                super.getDropDownView(position, convertView, parent);
                View v = super.getView(position, convertView, parent);
                TextView tv = v.findViewById(R.id.tvItemName);
                tv.setTextColor(ContextCompat.getColor(activity, R.color.orange_select));
                return v;
            }
        };
        //dataBinding.spinnerTime.setAdapter(mAdapterSpinner);

        dataBinding.spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = view.findViewById(R.id.tvItemName);
                textView.setTextColor(ContextCompat.getColor(activity, R.color.orange_select));
                calculateStartDate(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); */
        dataBinding.tvRange.setOnClickListener(this);
    //    dataBinding.spinnerTime.setSelection(3);
    }

    private void setData() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (transactionList != null && transactionList.size() > 0) {
                    dataBinding.groupNoResult.setVisibility(View.VISIBLE);
                    dataBinding.linChart.setVisibility(View.VISIBLE);
                    dataBinding.ivNoData.setVisibility(View.GONE);
                    dataBinding.tvNoResult.setVisibility(View.GONE);
                    dataBinding.scrollView.setBackgroundColor(ContextCompat.getColor(activity, R.color.gray_bg));


                    dataBinding.tvIncome.setText("+" + Pref.getCurrencySymbol(activity) + transactionList.get(0).getIncome());
                    dataBinding.tvExpense.setText("-" + Pref.getCurrencySymbol(activity) + transactionList.get(0).getExpense());

                    dataBinding.pieChart.setUsePercentValues(true);
                    dataBinding.pieChart.setExtraOffsets(0, 5, 0, 5);


                    dataBinding.pieChart.setDrawHoleEnabled(true);
                    dataBinding.pieChart.setHoleColor(Color.WHITE);
                    dataBinding.pieChart.setTransparentCircleColor(Color.WHITE);
                    dataBinding.pieChart.setTransparentCircleAlpha(110);

                    Logger.d("PieChart Height", dataBinding.pieChart.getHeight() + "");
                    dataBinding.pieChart.setHoleRadius(65f);
                    Logger.d("PieChart Height2", dataBinding.pieChart.getHeight() + "");

                    //                    dataBinding.pieChart.setTransparentCircleRadius(61f);

                    dataBinding.pieChart.setDrawCenterText(false);

                    dataBinding.pieChart.setRotationAngle(0);
                    dataBinding.pieChart.setRotationEnabled(true);
                    dataBinding.pieChart.setHighlightPerTapEnabled(true);

//                    dataBinding.pieChart.setExtraRightOffset(50);
//                    dataBinding.pieChart.offsetTopAndBottom(10);
                    dataBinding.pieChart.animateY(1000, Easing.EaseInOutCirc);

                    Legend l = dataBinding.pieChart.getLegend();
                    l.setEnabled(false);
//                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
//                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
//                    l.setDrawInside(true);
//                    l.setXEntrySpace(7f);
//                    l.setYEntrySpace(0f);
//                    l.setWordWrapEnabled(true);
//                    l.setXOffset(25f);
//                    l.setTextSize(13);
//                    l.setTypeface(ResourcesCompat.getFont(activity, R.font.source_sanspro_semibold));
                    // entry label styling
//                    dataBinding.pieChart.setExtraLeftOffset(10);
//                    dataBinding.pieChart.setExtraRightOffset(30);
                    dataBinding.pieChart.setDrawEntryLabels(false);


//                    dataBinding.pieChart.setEntryLabelColor(Color.BLACK);
//                    dataBinding.pieChart.setEntryLabelTextSize(12f);
//                    dataBinding.pieChart.setEntryLabelTypeface(ResourcesCompat.getFont(activity, R.font.source_sanspro_semibold));


                    ArrayList<PieEntry> entries = new ArrayList<>();
                    ArrayList<Integer> colors = new ArrayList<>();
                    List<CategoryModel> categoryList = new ArrayList<>();
                    for (DashboardItem transactionModel : transactionList) {
                        entries.add(new PieEntry((float) transactionModel.getTotal(), transactionModel.getCategory_name()));
                        colors.add(Color.parseColor(transactionModel.getCategory_color()));
                        if (transactionList.indexOf(transactionModel) < 7) {
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCategoryColor(transactionModel.getCategory_color());
                            categoryModel.setCategoryName(transactionModel.getCategory_name());
                            categoryList.add(categoryModel);
                        }
                    }


                    ChartCategoryListAdapter adapter = new ChartCategoryListAdapter(categoryList, activity);
                    dataBinding.rvChartCategory.setLayoutManager(new LinearLayoutManager(activity));
                    dataBinding.rvChartCategory.setItemAnimator(new DefaultItemAnimator());
                    dataBinding.rvChartCategory.setAdapter(adapter);

                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(colors);

                    dataBinding.pieChart.setDescription(null);
                    dataSet.setDrawIcons(false);
                    dataSet.setValueLineColor(Color.TRANSPARENT);
                    dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
                    dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

                    PieData data = new PieData(dataSet);
                    data.setValueFormatter(new PercentFormatter(dataBinding.pieChart));
                    data.setValueTextSize(10f);
                    data.setValueTypeface(ResourcesCompat.getFont(activity, R.font.source_sanspro_semibold));
                    data.setValueTextColor(Color.WHITE);
                    dataBinding.pieChart.setData(data);
                    dataBinding.pieChart.invalidate();
                    setTransactionAdapter();
                } else {
                    dataBinding.groupNoResult.setVisibility(View.GONE);
                    dataBinding.ivNoData.setVisibility(View.VISIBLE);
                    dataBinding.linChart.setVisibility(View.GONE);
                    dataBinding.tvNoResult.setVisibility(View.VISIBLE);
                    dataBinding.scrollView.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                }
            }
        });
    }

    private void calculateStartDate(int position) {
        dataBinding.tvRange.setVisibility(View.GONE);
        switch (position) {
//            case 0: {
//                endDate = System.currentTimeMillis() / 1000;
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, 0);
//                calendar.clear(Calendar.MINUTE);
//                calendar.clear(Calendar.SECOND);
//                calendar.clear(Calendar.MILLISECOND);
//                startDate = calendar.getTimeInMillis() / 1000;
//                Logger.d("Today Start Date", startDate + " ");
//                getDashBoardData();
//                break;
//            }
            case 0: {
                endDate = System.currentTimeMillis() / 1000;
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                calendar.clear(Calendar.MILLISECOND);
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                startDate = calendar.getTimeInMillis() / 1000;
 //               Logger.d("Week Start Date", startDate + " " + calendar.getFirstDayOfWeek());
                getDashBoardData();
                break;
            }
            case 1: {
                endDate = System.currentTimeMillis() / 1000;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                calendar.clear(Calendar.MILLISECOND);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = calendar.getTimeInMillis() / 1000;
                Logger.d("Month Start Date", startDate + " ");
                getDashBoardData();
                break;
            }
            case 2: {
                endDate = System.currentTimeMillis() / 1000;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                calendar.clear(Calendar.MILLISECOND);
                calendar.clear(Calendar.MONTH);
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                startDate = calendar.getTimeInMillis() / 1000;
                Logger.d("Year Start Date", startDate + " ");
                getDashBoardData();
                break;
            }
            case 3:
                startDate = 0;
                endDate = System.currentTimeMillis() / 1000;
                getDashBoardData();
                break;
            case 4:
                openRangeDialog(-1, -1);
                break;
        }
    }

    public void openRangeDialog(long start, long end) {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Date Range");
        if (start != -1 && end != -1) {
            builder.setSelection(new Pair<Long, Long>(start, end));
        }
        builder.setTheme(R.style.DateDialogTheme);
        MaterialDatePicker picker = builder.build();
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> pair = (Pair<Long, Long>) selection;
                startDate = (pair.first) / 1000;
                endDate = (pair.second) / 1000;
                dataBinding.tvRange.setVisibility(View.VISIBLE);
                dataBinding.tvRange.setText(activity.getDateFormatRange(startDate) + " - " + activity.getDateFormatRange(endDate));
                getDashBoardData();
            }
        });
        picker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                endDate = System.currentTimeMillis() / 1000;
                dataBinding.tvRange.setVisibility(View.GONE);
            }
        });
        picker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDate = System.currentTimeMillis() / 1000;
                dataBinding.tvRange.setVisibility(View.GONE);
            }
        });
        picker.show(activity.getSupportFragmentManager(), "DateRange");

    }

    private void getDashBoardData() {
        DayBookDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<DashboardItem> temp = DayBookDataBase.getDatabase(activity).transactions().getDashBoardTransactions(startDate, endDate);
                Logger.d("Transaction", temp.size() + " ");
                transactionList.clear();
                transactionList.addAll(temp);
                setData();
            }
        });
    }

    private void setTransactionAdapter() {
        if (transactionList != null && transactionList.size() > 0) {
            DashboardItemListAdapter mAdapter = new DashboardItemListAdapter(transactionList, activity, this, 1);
            dataBinding.rvTransaction.setLayoutManager(new LinearLayoutManager(activity));
            dataBinding.rvTransaction.setItemAnimator(new DefaultItemAnimator());
            dataBinding.rvTransaction.setAdapter(mAdapter);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onItemClicked(int position, View v, int type) {
        ArrayList<TransactionModel> temp = new ArrayList<>();

        HomeFragment fragment = (HomeFragment) getParentFragment();
        if (fragment != null) {
            fragment.setTransactionTab(2, startDate, endDate, transactionList.get(position).getCategory_id());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult Dashboard");
        if (requestCode == Constants.ADD_TRANSACTION_REQ_CODE && resultCode == Constants.ADD_TRANSACTION_RESULT_CODE) {
            Logger.d("onActivityResult Dashboard IF");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRange:
                openRangeDialog(startDate * 1000, endDate * 1000);
                break;
        }
    }
}
