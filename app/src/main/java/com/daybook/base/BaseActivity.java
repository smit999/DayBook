package com.daybook.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.daybook.global.Pref;
import com.google.android.material.textfield.TextInputEditText;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class BaseActivity extends AppCompatActivity {

    public Fragment getCurrentFragment(int contsinerId) {
        return getSupportFragmentManager().findFragmentById(contsinerId);
    }

    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setFragment(Fragment fragment, int container, boolean isAdd, boolean isBackStack, int anim[]) {
        try {
            hideKeyboard(findViewById(container));
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            if (anim != null) {
                ft.setCustomAnimations(anim[0], anim[1]);
            }

            if (isAdd) {
                ft.add(container, fragment, fragment.getClass().getSimpleName());
            } else {
                ft.replace(container, fragment, fragment.getClass().getSimpleName());
            }


            if (isBackStack) {
                ft.addToBackStack(fragment.getClass().getName());
            }


            Fragment currentFragment = getCurrentFragment(container);
            if (currentFragment != null) {
                ft.hide(currentFragment);
            }


            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setChildFragment(FragmentManager childFragmentManager, Fragment fragment, int container, boolean isAdd, boolean isBackStack) {
        FragmentTransaction ft = childFragmentManager.beginTransaction();
        if (isAdd) {
            ft.add(container, fragment, fragment.getClass().getSimpleName());
        } else {
            ft.replace(container, fragment, fragment.getClass().getSimpleName());
        }

        if (isBackStack) {
            ft.addToBackStack(null);
        }

        if (isAdd) {
            Fragment currentFragment = getCurrentFrgChild(childFragmentManager, container);
            if (currentFragment != null) {
                ft.hide(currentFragment);
            }
        }
        ft.commit();
    }

    public Fragment getCurrentFrgChild(FragmentManager childFragmentManager, int containerID) {
        return childFragmentManager.findFragmentById(containerID);
    }


    public boolean isOffline(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return true;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.isConnected()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getDisplayWidth() {
        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getDisplayHeight() {
        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public void popAllFragment() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
            getSupportFragmentManager().popBackStack();
        }
    }


    public String getFormattedValue(double value) {
        return new DecimalFormat("##.##").format(value);
    }


    public Date getDateFormat(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy"/* h:mm a"*/, Locale.ENGLISH);
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDateFormatRange(long timeInSeconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        return simpleDateFormat.format(new Date(timeInSeconds * 1000));
    }


    public String getStringDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy"/* h:mm a"*/, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

    public String getDateTimeFormate(long timeStampSecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy h:mm a", Locale.ENGLISH);
        return simpleDateFormat.format(new Date(timeStampSecond * 1000));//convert mili second
    }


    public void openKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showToast(final String string, final boolean isLong) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, string, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showToast(final int stringId, final boolean isLong) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, getStrings(stringId), isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String getAllDateFormat(String startDate, String dateFormat) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            return simpleDateFormat.format(startDate);
        } catch (Exception e) {
            e.printStackTrace();
            return startDate;
        }
    }


    public String encrypt(String value, String key, String IV) throws GeneralSecurityException {
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(IV.getBytes()));
        byte[] encrypted = cipher.doFinal(value.getBytes());
        String encryptedS = byteArrayToHexString(encrypted);
        return encryptedS;
    }

    public String decrypt(String message, String key, String IV) throws GeneralSecurityException {
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(IV.getBytes()));
        byte[] decordedValue = hexStringToByteArray(message);
        byte[] decrypted = cipher.doFinal(decordedValue);
        String decryptedValue = new String(decrypted);
        return decryptedValue;
    }

    public String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public boolean isPackageInstalled(Context context, String packageName) {
        PackageManager var1 = context.getPackageManager();

        try {
            var1.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException var3) {
            return false;
        }
    }

    public String getStrings(int stringId) {
        String result;
        Configuration config = new Configuration(getResources().getConfiguration());
        Locale requestedLocale;
        if (Pref.getLanguage(this) == 2) {
            requestedLocale = new Locale("hi");
        } else if (Pref.getLanguage(this) == 3) {
            requestedLocale = new Locale("gu");
        } else {
            requestedLocale = new Locale("en");
        }
        config.setLocale(requestedLocale);
        result = createConfigurationContext(config).getText(stringId).toString();
        return result;
    }


    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public boolean checkEmpty(TextInputEditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }


    public int getResourceId(String name) {
        return getResources().getIdentifier((name != null && !name.equals("") ? name : "other"), "drawable", getPackageName());
    }

    public int calculateNoOfColumns(Context context, float columnWidthDp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }
}