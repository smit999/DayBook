package com.daybook.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.base.BaseFragment;
import com.daybook.databinding.FragmentAddNewTransactionBinding;
import com.daybook.db.DayBookDataBase;
import com.daybook.db.model.CategoryModel;
import com.daybook.db.model.TransactionModel;
import com.daybook.global.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class AddNewTransactionFragment extends BaseFragment implements View.OnClickListener {
    private static final String TRANSACTION = "TRANSACTION";
    private HomeActivity activity;
    private FragmentAddNewTransactionBinding dataBinding;
    private CategoryModel categoryModel;
    private Calendar selectedCalendar;
    private TransactionModel transactionModel;
    String[] array;
    private static final int CAMERA_CODE = 113;
    private final int STORAGE_PERMISSION_CODE = 11;
    private int GALLERY_CODE = 12;
    private File selectedFile = null;


    public static AddNewTransactionFragment getInstance(TransactionModel transactionModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TRANSACTION, transactionModel);
        AddNewTransactionFragment fragment = new AddNewTransactionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_transaction, container, false);
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
        dataBinding.actionBar.tvTitle.setText(activity.getStrings(R.string.title_add_transaction));
        dataBinding.actionBar.ivBack.setVisibility(View.VISIBLE);
        dataBinding.actionBar.ivBack.setOnClickListener(this);
        dataBinding.etCategory.setOnClickListener(this);
        dataBinding.btnAddTransaction.setOnClickListener(this);
        dataBinding.etDate.setOnClickListener(this);

        array = new String[]{"Select Paid By", "Credit Card", "Debit Card", "Cash", "Wallet", "Bank Transfer"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, array) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Typeface typeface = dataBinding.etAmount.getTypeface();
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTypeface(typeface);
                v.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen._11ssp));
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                super.getDropDownView(position, convertView, parent);
                Typeface typeface = dataBinding.etAmount.getTypeface();
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setPadding(20, 20, 20, 20);
                v.setTypeface(typeface);
                v.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen._11ssp));
                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataBinding.etPaidBy.setAdapter(adapter);
        selectedCalendar = Calendar.getInstance();
        dataBinding.etDate.setText(activity.getDateFormatRange(selectedCalendar.getTimeInMillis() / 1000));
        dataBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                categoryModel = null;
                dataBinding.etCategory.setText("");
                if (tab.getPosition() == 0) {
                    dataBinding.btnAddTransaction.setText(R.string.text_add_expense);
                } else {
                    dataBinding.btnAddTransaction.setText(R.string.text_add_income);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        if (getArguments() != null && getArguments().getParcelable(TRANSACTION) != null) {
            transactionModel = getArguments().getParcelable(TRANSACTION);
            dataBinding.actionBar.tvTitle.setText("Update Transaction");
            updateUI();
        }
    }

    private void updateUI() {
        dataBinding.etAmount.setText(transactionModel.getAmount() + "");
        dataBinding.etDate.setText(activity.getDateFormatRange(transactionModel.getTimeStamp()));
        dataBinding.etSubject.setText(transactionModel.getSubject());
        dataBinding.etCategory.setText(transactionModel.getCategoryName());
        dataBinding.tabLayout.getTabAt(transactionModel.getTransactionType() == 1 ? 0 : 1).select();
        dataBinding.btnAddTransaction.setText(transactionModel.getTransactionType() == 2 ? R.string.text_add_income : R.string.text_add_expense);
        dataBinding.etDescription.setText(transactionModel.getTransactionInfo());

        for (int i = 0; i < array.length; i++) {
            if (transactionModel.getPaidBy().equalsIgnoreCase(array[i])) {
                dataBinding.etPaidBy.setSelection(i);
                break;
            }
        }

        categoryModel = new CategoryModel();
        categoryModel.setCategoryIcon(transactionModel.getCategoryIcon());
        categoryModel.setCategoryColor(transactionModel.getCategoryColor());
        categoryModel.setCategoryName(transactionModel.getCategoryName());
        categoryModel.setId(transactionModel.getCategoryId());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                activity.onBackPressed();
                break;
            case R.id.etCategory:
                CategoryListFragment fragment = CategoryListFragment.getInstance(dataBinding.tabLayout.getSelectedTabPosition() == 0 ? 1 : 2);
                fragment.setTargetFragment(this, Constants.SELECT_CATEGORY_REQ_CODE);
                activity.setFragment(fragment, R.id.mainFrame, true, true, null);
                break;
            case R.id.btnAddTransaction:
                if (validate()) {
                    addNewTransaction();
                }
                break;
            case R.id.etDate:
                if (!dataBinding.etDate.getText().toString().trim().equals("")) {
                    selectedCalendar.setTime(activity.getDateFormat(dataBinding.etDate.getText().toString().trim()));
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.DateDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        // Get Current Time
                        final Calendar c = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);
                        dataBinding.etDate.setText(activity.getStringDate(selectedCalendar.getTime()));
                    }
                }, selectedCalendar
                        .get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH),
                        selectedCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                break;
        }
    }

    private void addNewTransaction() {
        final TransactionModel model = new TransactionModel();
        model.setSubject(dataBinding.etSubject.getText().toString().trim());
        model.setTransactionInfo(dataBinding.etDescription.getText().toString());
        model.setAmount(Double.parseDouble(dataBinding.etAmount.getText().toString().trim()));
        model.setTransactionType(dataBinding.tabLayout.getSelectedTabPosition() == 0 ? 1 : 2);
        model.setCategoryName(categoryModel.getCategoryName());
        model.setCategoryId(categoryModel.getId());
        model.setCategoryColor(categoryModel.getCategoryColor());
        model.setCategoryIcon(categoryModel.getCategoryIcon());
        model.setPaidBy(array[dataBinding.etPaidBy.getSelectedItemPosition()]);
        DayBookDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (transactionModel != null) {
                    model.setTransactionId(transactionModel.getTransactionId());
                    model.setAttachmentImage(selectedFile != null ? selectedFile.getAbsolutePath() : transactionModel.getAttachmentImage());
                    model.setTimeStamp(transactionModel.getTimeStamp());
                    DayBookDataBase.getDatabase(activity).transactions().updateTransaction(model);
                } else {
                    model.setTimeStamp(selectedCalendar.getTimeInMillis() / 1000);
                    model.setAttachmentImage(selectedFile != null ? selectedFile.getAbsolutePath() : "");
                    DayBookDataBase.getDatabase(activity).transactions().insert(model);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.showToast("Transaction added successfully", true);
                        if (getTargetFragment() != null)
                            getTargetFragment().onActivityResult(Constants.ADD_TRANSACTION_REQ_CODE, Constants.ADD_TRANSACTION_RESULT_CODE, null);
                        activity.onBackPressed();
                    }
                });
            }
        });
    }

    private boolean validate() {
        if (dataBinding.etSubject.getText().toString().trim().length() == 0) {
            activity.showToast(R.string.warn_empty_subject, false);
        } else if (dataBinding.etAmount.getText().toString().trim().length() == 0) {
            activity.showToast(R.string.warn_empty_amount, false);
        } else if (dataBinding.etDate.getText().toString().trim().length() == 0) {
            activity.showToast(R.string.warn_empty_date, false);
        } else if (dataBinding.etPaidBy.getSelectedItemPosition() == 0) {
            activity.showToast(R.string.warn_select_paid_by, false);
        } else if (dataBinding.etCategory.getText().toString().trim().length() == 0) {
            activity.showToast(R.string.warn_select_category, false);
        } else {
            return true;
        }
        return false;
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != (PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SELECT_CATEGORY_REQ_CODE && resultCode == Constants.SELECT_CATEGORY_RESULT) {
            categoryModel = data.getParcelableExtra("CATEGORY");
            dataBinding.etCategory.setText(categoryModel.getCategoryName());
        } else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            if (data != null && data.getData() != null) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(activity.getContentResolver(), data.getData()));
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
                    }
                    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteOutput);

                    File dayBook = new File(activity.getFilesDir(), Constants.TRANSACTION_IMAGE_DIRECTORY);
                    if (!dayBook.exists()) {
                        dayBook.mkdirs();
                    }

                    selectedFile = new File(dayBook + File.separator + "transaction_" + (System.currentTimeMillis() / 1000) + ".jpg");
                    try {
                        FileOutputStream fo = new FileOutputStream(selectedFile);
                        fo.write(byteOutput.toByteArray());
                        fo.flush();
                        fo.close();

                        UCrop.of(Uri.fromFile(selectedFile), Uri.fromFile(selectedFile))
                                .start(activity, this);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, "Something going wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            activity.showToast("Something going wrong please try again.", false);
        }
    }
}
