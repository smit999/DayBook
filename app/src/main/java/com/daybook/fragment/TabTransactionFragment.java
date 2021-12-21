package com.daybook.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.adapter.TimeSpinnerAdapter;
import com.daybook.adapter.TransactionListAdapter;
import com.daybook.base.BaseFragment;
import com.daybook.customdialog.TransactionDetailsDialog;
import com.daybook.databinding.FragmentTransactionTabBinding;
import com.daybook.db.DayBookDataBase;
import com.daybook.db.dao.TransactionDao;
import com.daybook.db.model.CategoryModel;
import com.daybook.db.model.TransactionModel;
import com.daybook.global.Constants;
import com.daybook.listener.ItemOnClickListener;
import com.daybook.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class TabTransactionFragment extends BaseFragment implements ItemOnClickListener, View.OnClickListener {
    private static final String CALL_FROM = "CALL_FROM";
    private static final String CATEGORY_ID = "CATEGORY_ID";
    private static final String START_DATE = "START_DATE";
    private static final String END_DATE = "END_DATE";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private static final String CATEGORY_ICON = "CATEGORY_ICON";
    private HomeActivity activity;
    private FragmentTransactionTabBinding dataBinding;
    private int callFrom;
    private List<TransactionModel> transactionList = new ArrayList<>();
    private TransactionListAdapter mAdapter;
    private long startDate = System.currentTimeMillis() / 1000;
    private long endDate = System.currentTimeMillis() / 1000;
    private int categoryId;
    private BaseAdapter categoryAdapter;
    private List<CategoryModel> categoryList = new ArrayList<>();
    private int selectedCategoryId = 0;

    public static TabTransactionFragment getInstance(int callFrom, long startDate, long endDate, int categoryId, String category_name, String categoryIcon) {
        Bundle bundle = new Bundle();
        bundle.putInt(CALL_FROM, callFrom);
        bundle.putInt(CATEGORY_ID, categoryId);
        bundle.putLong(START_DATE, startDate);
        bundle.putLong(END_DATE, endDate);
        bundle.putString(CATEGORY_NAME, category_name);
        bundle.putString(CATEGORY_ICON, categoryIcon);
        TabTransactionFragment fragment = new TabTransactionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_tab, container, false);
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

        dataBinding.tvTitle.setText(activity.getStrings(R.string.title_transactions));
        callFrom = getArguments().getInt(CALL_FROM);
        if (callFrom == 2) {
            categoryId = getArguments().getInt(CATEGORY_ID);
            startDate = getArguments().getLong(START_DATE);
            endDate = getArguments().getLong(END_DATE);
            dataBinding.groupFilter.setVisibility(View.VISIBLE);
        } else {
            endDate = System.currentTimeMillis() / 1000;
            startDate = 0;
            dataBinding.groupFilter.setVisibility(View.GONE);
        }
        getCategoryAndSet();
        List<String> types = Arrays.asList(new String[]{"All", "Expense", "Income"});
        TimeSpinnerAdapter mAdapterSpinner2 = new TimeSpinnerAdapter(activity, types, this, 2);
        dataBinding.spinnerType.setAdapter(mAdapterSpinner2);

        dataBinding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getTransactionFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dataBinding.floatAdd.setOnClickListener(this);
        dataBinding.ivFilter.setOnClickListener(this);
        getTransactionFromDB();
    }

    private void getCategoryAndSet() {
        DayBookDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<CategoryModel> categoryModels = DayBookDataBase.getDatabase(activity).categoryDao().getAllCategories();
                categoryList.clear();
                categoryList.addAll(categoryModels);
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setCategoryName("All Category");
                categoryModel.setId(0);
                categoryList.add(0, categoryModel);
                activity.runOnUiThread(() -> setCategoryAdapter());
            }
        });
    }

    private void getTransactionFromDB() {
        DayBookDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<TransactionModel> temp = new ArrayList<>();
                TransactionDao transactionDao = DayBookDataBase.getDatabase(activity).transactions();
                if (dataBinding.spinnerType.getSelectedItemPosition() == 0) {
                    temp = selectedCategoryId == 0 ? transactionDao.getTransactions(startDate, endDate)
                            : transactionDao.getTransactions(startDate, endDate, selectedCategoryId);
                } else {
                    temp = selectedCategoryId == 0 ? transactionDao.getTransactionsByType(dataBinding.spinnerType.getSelectedItemPosition() == 1 ? 1
                            : 2, startDate, endDate)
                            : transactionDao.getTransactionsByType(dataBinding.spinnerType.getSelectedItemPosition() == 1 ? 1
                            : 2, startDate, endDate, selectedCategoryId);
                }
                transactionList.clear();
                transactionList.addAll(temp);
                setAdapter();
            }
        });
    }

    private void calculateStartDate(int position) {
        switch (position) {
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
                Logger.d("Week Start Date", startDate + " ");
                getTransactionFromDB();
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
                getTransactionFromDB();
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
                getTransactionFromDB();
                break;
            }
        }
    }

    private void setAdapter() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (transactionList == null || transactionList.size() == 0) {
                    dataBinding.noResult.getRoot().setVisibility(View.VISIBLE);
                    dataBinding.rvTransaction.setVisibility(View.GONE);
                } else {
                    dataBinding.noResult.getRoot().setVisibility(View.GONE);
                    dataBinding.rvTransaction.setVisibility(View.VISIBLE);
                    if (mAdapter == null) {
                        mAdapter = new TransactionListAdapter(transactionList, activity, TabTransactionFragment.this, 2);
                        dataBinding.rvTransaction.setLayoutManager(new LinearLayoutManager(activity));
                        dataBinding.rvTransaction.setItemAnimator(new DefaultItemAnimator());
                        dataBinding.rvTransaction.setAdapter(mAdapter);
                        dataBinding.rvTransaction.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.set(0, 0, 0, 5);
                            }
                        });
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void setCategoryAdapter() {
        if (categoryAdapter == null) {
            categoryAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return categoryList.size();
                }

                @Override
                public Object getItem(int position) {
                    return categoryList.get(position).getCategoryName();
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = LayoutInflater.from(activity).inflate(R.layout.item_single_list, parent, false);
                    TextView textView = view.findViewById(R.id.tvItemName);
                    textView.setText(categoryList.get(position).getCategoryName());
                    return textView;
                }
            };

            dataBinding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedCategoryId = categoryList.get(i).getId();
                    getTransactionFromDB();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            dataBinding.spinnerCategory.setAdapter(categoryAdapter);
            if (categoryId != 0 && callFrom == 2) {
                for (int i = 0; i < categoryList.size(); i++) {
                    if (categoryId == categoryList.get(i).getId()) {
                        dataBinding.spinnerCategory.setSelection(i);
                        break;
                    }
                }
            }
        } else {
            categoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClicked(int position, View v, int type) {
        if (type == 2) {//delete
            new AlertDialog.Builder(activity).setTitle("Delete Transaction")
                    .setMessage("Are you sure want to delete this transaction?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteTransaction(transactionList.get(position).getTransactionId());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else if (type == 3) {//edit
            AddNewTransactionFragment fragment = AddNewTransactionFragment.getInstance(transactionList.get(position));
            fragment.setTargetFragment((HomeFragment) getParentFragment(), Constants.ADD_TRANSACTION_REQ_CODE);
            activity.setFragment(fragment, R.id.mainFrame, true, true, null);
        } else if (type == 4) {
            TransactionDetailsDialog detailsDialog = new TransactionDetailsDialog(activity,
                    android.R.style.Theme_Material_Light_Dialog_NoActionBar,
                    transactionList.get(position).getAttachmentImage(),
                    transactionList.get(position).getTransactionInfo());
            detailsDialog.show();
        }
    }

    private void deleteTransaction(int transactionId) {
        DayBookDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DayBookDataBase.getDatabase(activity).transactions().deleteTransaction(transactionId);
                getTransactionFromDB();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                activity.onBackPressed();
                break;
            case R.id.floatAdd:
                AddNewTransactionFragment fragment = AddNewTransactionFragment.getInstance(null);
                fragment.setTargetFragment((HomeFragment) getParentFragment(), Constants.ADD_TRANSACTION_REQ_CODE);
                activity.setFragment(fragment, R.id.mainFrame, true, true, null);

            break;
            case R.id.ivFilter:
                if (dataBinding.groupFilter.getVisibility() == View.VISIBLE)
                    dataBinding.groupFilter.setVisibility(View.GONE);
                else
                    dataBinding.groupFilter.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult Transaction");
        if (requestCode == Constants.ADD_TRANSACTION_REQ_CODE && resultCode == Constants.ADD_TRANSACTION_RESULT_CODE) {
            Logger.d("onActivityResult Transaction IF");
            endDate = System.currentTimeMillis() / 1000;
            getTransactionFromDB();
        } else if (requestCode == Constants.DASHBOARD_TRANSACTION_REQ) {
            callFrom = data.getIntExtra("CALL_FROM", 2);
            categoryId = data.getIntExtra("CATEGORY_ID", 0);
            startDate = data.getLongExtra("START_DATE", 0);
            endDate = data.getLongExtra("END_DATE", 0);
            dataBinding.groupFilter.setVisibility(View.VISIBLE);

            if (categoryId != 0 && callFrom == 2) {
                for (int i = 0; i < categoryList.size(); i++) {
                    if (categoryId == categoryList.get(i).getId()) {
                        dataBinding.spinnerCategory.setSelection(i);
                        break;
                    }
                }
            }
        } else if (requestCode == Constants.TAB_CHANGE_REQ) {
            callFrom = 1;
            categoryId = 0;
            selectedCategoryId = 0;
            endDate = System.currentTimeMillis() / 1000;
            startDate = 0;
            dataBinding.groupFilter.setVisibility(View.GONE);
            dataBinding.spinnerCategory.setSelection(0);
            getTransactionFromDB();
        }
    }
}