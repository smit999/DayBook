package com.daybook.customdialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialog;
import androidx.databinding.DataBindingUtil;

import com.daybook.R;
import com.daybook.databinding.DialogLoadingEventBinding;


public class LoadingEventDialog extends AppCompatDialog {
    private DialogLoadingEventBinding dataBinding;
    private Context context;
    private String event;

    public LoadingEventDialog(Context context, String event) {
        super(context);
        this.context = context;
        this.event = event;
    }

    public void setLoadingEvent(String event) {
        this.event = event;
        dataBinding.tvEventName.setText(event);
        dataBinding.invalidateAll();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_event, null, false);
        setContentView(view);
        dataBinding = DataBindingUtil.bind(view);
        dataBinding.tvEventName.setText(event);
    }
}
