package com.daybook.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.adapter.CategoryListAdapter;
import com.daybook.base.BaseFragment;
import com.daybook.databinding.FragmentCategoryListBinding;
import com.daybook.db.DayBookDataBase;
import com.daybook.db.model.CategoryModel;
import com.daybook.global.Constants;
import com.daybook.listener.ItemOnClickListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryListFragment extends BaseFragment implements ItemOnClickListener, View.OnClickListener {
    private static final String CALL_FROM = "CALL_FROM";
    private HomeActivity activity;
    private FragmentCategoryListBinding dataBinding;
    private List<CategoryModel> categoryModelList = new ArrayList<>();
    private CategoryListAdapter adapter;
    private int callFrom;

    public static CategoryListFragment getInstance(int callFrom) {//call_from 1 for Expense, 2 for income//3 for all
        Bundle bundle = new Bundle();
        bundle.putInt(CALL_FROM, callFrom);
        CategoryListFragment fragment = new CategoryListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false);
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
        dataBinding.actionBar.tvTitle.setText(activity.getStrings(R.string.title_category_list));
        dataBinding.actionBar.ivBack.setVisibility(View.VISIBLE);
        dataBinding.actionBar.ivBack.setOnClickListener(this);
        callFrom = getArguments().getInt(CALL_FROM);
        getCategoryList();

    }

    private void getCategoryList() {
        DayBookDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<CategoryModel> temp;
                if (callFrom == 3) {
                    temp = DayBookDataBase.getDatabase(activity).categoryDao().getAllCategories();
                } else {
                    temp = DayBookDataBase.getDatabase(activity).categoryDao().getAllCategoriesByType(callFrom);
                }
                categoryModelList.clear();
                categoryModelList.addAll(temp);
                setAdapter();
            }
        });
    }

    private void setAdapter() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter == null) {
                    adapter = new CategoryListAdapter(categoryModelList, activity, CategoryListFragment.this);
                    dataBinding.rvCategory.setLayoutManager(new LinearLayoutManager(activity));
                    dataBinding.rvCategory.setItemAnimator(new DefaultItemAnimator());
                    dataBinding.rvCategory.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onItemClicked(int position, View v, int type) {// type 1 for select , 2 for delete
        if (type == 1) {
            if (getTargetFragment() != null) {
                Intent intent = new Intent();
                intent.putExtra("CATEGORY", categoryModelList.get(position));
                getTargetFragment().onActivityResult(Constants.SELECT_CATEGORY_REQ_CODE, Constants.SELECT_CATEGORY_RESULT, intent);
                activity.onBackPressed();
            }
        } else if (type == 2) {
            DayBookDataBase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                 int result =  DayBookDataBase.getDatabase(activity).transactions().checkCategoryInTransaction(categoryModelList.get(position).getId());
                 activity.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         if(result == 0){
                             new AlertDialog.Builder(activity).setMessage("Are you sure want to delete this category?")
                                     .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialogInterface, int i) {
                                             deleteCategory(position);
                                         }
                                     })
                                     .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialogInterface, int i) {
                                             dialogInterface.dismiss();
                                         }
                                     })
                                     .create().show();
                         }
                         else {
                             Toast.makeText(activity, "You can not delete this category. Transaction added in this category.", Toast.LENGTH_LONG).show();
                         }
                     }
                 });
                }
            });

        }
    }

    private void deleteCategory(final int position) {
        DayBookDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DayBookDataBase.getDatabase(activity).categoryDao().deleteCategory(categoryModelList.get(position).getId());
                getCategoryList();
                activity.showToast("Category deleted successfully", false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                activity.onBackPressed();
                break;
        }
    }

    private void addCategoryInDB(final CategoryModel categoryModel) {
        DayBookDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DayBookDataBase.getDatabase(activity).categoryDao().insert(categoryModel);
                getCategoryList();
                activity.showToast("Category Added Successfully", false);
            }
        });
    }
}
