package com.daybook.customdialog;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialog;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.daybook.R;
import com.daybook.activity.HomeActivity;
import com.daybook.databinding.DialogTransactionDetailsBinding;

import java.io.File;

public class TransactionDetailsDialog extends AppCompatDialog {
    private String file;
    private String transactionInfo;
    private HomeActivity activity;

    public TransactionDetailsDialog(HomeActivity context, int theme, String file, String transactionInfo) {
        super(context, theme);
        this.file = file;
        this.transactionInfo = transactionInfo;
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogTransactionDetailsBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_transaction_details, null, false);
        setContentView(dataBinding.getRoot());
        getWindow().setLayout((int) (activity.getDisplayWidth() * 0.9), (int) (activity.getDisplayHeight() * 0.8));

        if (transactionInfo == null || transactionInfo.equals(""))
            dataBinding.tvTransactionDetails.setVisibility(View.GONE);
        else
            dataBinding.tvTransactionDetails.setText(transactionInfo);


        if (file == null || file.equals("")) {
            dataBinding.ivAttachment.setVisibility(View.GONE);
        } else {
            Glide.with(activity)
                    .load(new File(file))
                    .into(dataBinding.ivAttachment);

        }

        dataBinding.btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
