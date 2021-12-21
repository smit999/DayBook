package com.daybook.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.daybook.R;
import com.daybook.base.BaseActivity;
import com.daybook.db.DayBookDataBase;
import com.daybook.fragment.HomeFragment;
import com.daybook.global.Constants;
import com.daybook.global.Pref;
import com.daybook.utils.Logger;

import java.util.Calendar;

public class HomeActivity extends BaseActivity {
    public DayBookDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setFragment(new HomeFragment(), R.id.mainFrame, false, false, null);
        db = DayBookDataBase.getDatabase(this);
        if (!Pref.getAlarmSetupCompleted(this)) {
            Calendar after8PmCal = Calendar.getInstance();
            if (after8PmCal.get(Calendar.HOUR_OF_DAY) >= 20) {
                after8PmCal.set(Calendar.DAY_OF_YEAR, after8PmCal.get(Calendar.DAY_OF_YEAR) + 1);
                after8PmCal.set(Calendar.HOUR_OF_DAY, 20);
                after8PmCal.set(Calendar.MINUTE, 0);
                after8PmCal.set(Calendar.SECOND, 0);

                Logger.d("Calender", after8PmCal.getTimeInMillis() + " ");
            } else {
                after8PmCal.set(Calendar.HOUR_OF_DAY, 20);
                after8PmCal.set(Calendar.MINUTE, 0);
                after8PmCal.set(Calendar.SECOND, 0);
                Logger.d("Calender2", after8PmCal.getTimeInMillis() + " ");
            }
            Pref.setAlarmedSetupCompleted(this, true);
        }
    }

    public void showAlertForPermission(final String permission, final int requestCode, final boolean isLocationPrmission) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage("Please goto setting and grant " + permission).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, requestCode);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isLocationPrmission) {
                    Toast.makeText(HomeActivity.this, "Location Permission must be required. Please go to settings and give location permission.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(HomeActivity.this, permission + " must be required. Please go to settings and give ." + permission, Toast.LENGTH_LONG).show();
                }
            }
        }).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment(R.id.mainFrame) != null && getCurrentFragment(R.id.mainFrame) instanceof HomeFragment)
            getCurrentFragment(R.id.mainFrame).onActivityResult(Constants.BACK_PRESS_REQ, 200, null);
        else
            super.onBackPressed();
    }
}
