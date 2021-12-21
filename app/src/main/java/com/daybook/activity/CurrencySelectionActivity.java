package com.daybook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daybook.R;
import com.daybook.adapter.CurrencySelectionAdapter;
import com.daybook.base.BaseActivity;
import com.daybook.databinding.ActivityCurrencySelectionBinding;
import com.daybook.db.model.CurrencyModel;
import com.daybook.global.Pref;
import com.daybook.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class CurrencySelectionActivity extends BaseActivity implements View.OnClickListener {
    private ActivityCurrencySelectionBinding dataBinding;
    private List<CurrencyModel> currencyList;
    private List<CurrencyModel> searchList = new ArrayList<>();
    private CurrencySelectionAdapter adapter;
    private CurrencyModel lastSelectedCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_currency_selection);
        getCurrencyList();
        dataBinding.ivBack.setOnClickListener(this);
        dataBinding.ivTick.setOnClickListener(this);
        dataBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchInList();
            }
        });
    }

    private void searchInList() {
        searchList.clear();
        if (dataBinding.etSearch.getText().toString().trim().length() > 0) {
            for (CurrencyModel currencyModel : currencyList) {
                if (currencyModel.getSort_name().toLowerCase().contains(dataBinding.etSearch.getText().toString().trim().toLowerCase())) {
                    searchList.add(currencyModel);
                }
            }
        } else {
            searchList.addAll(currencyList);
        }
        setAdapter();
    }

    private void getCurrencyList() {
        InputStream is = getResources().openRawResource(R.raw.currency_symbols_list);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        try {
            JSONArray jsonObject = new JSONArray(jsonString);
            Logger.d("JSONArray", jsonObject.toString());

            currencyList = new Gson().fromJson(jsonObject.toString(),
                    new TypeToken<List<CurrencyModel>>() {
                    }.getType());
            searchList.addAll(currencyList);
            setAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setAdapter() {
        if (adapter == null) {
            adapter = new CurrencySelectionAdapter(this, searchList, (position, v, type) -> {
                if(lastSelectedCurrency == null){
                    searchList.get(position).setSelected(true);
                    lastSelectedCurrency = searchList.get(position);
                    adapter.notifyDataSetChanged();
                }
                else if((currencyList.indexOf(lastSelectedCurrency) != currencyList.indexOf(searchList.get(position)))){
                    lastSelectedCurrency.setSelected(false);
                    searchList.get(position).setSelected(true);
                    lastSelectedCurrency = searchList.get(position);
                    adapter.notifyDataSetChanged();
                }
                Pref.setCurrencySymbol(CurrencySelectionActivity.this, lastSelectedCurrency.getSymbol());
            });
            dataBinding.rvCurrency.setLayoutManager(new LinearLayoutManager(this));
            dataBinding.rvCurrency.setItemAnimator(new DefaultItemAnimator());
            dataBinding.rvCurrency.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivTick:
                Intent intent = new Intent(CurrencySelectionActivity.this, HomeActivity.class);
                startActivity(intent);
                finishAffinity();
                break;
        }
    }
}
