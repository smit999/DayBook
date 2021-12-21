package com.daybook.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class DashboardItem {
    private String category_color;
    private String category_icon;
    private int transaction_count;
    private double total;
    private String category_name;
    private double expense;
    private double income;
    private int category_id;

    public String getCategory_color() {
        return category_color;
    }

    public void setCategory_color(String category_color) {
        this.category_color = category_color;
    }

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
    }

    public int getTransaction_count() {
        return transaction_count;
    }

    public void setTransaction_count(int transaction_count) {
        this.transaction_count = transaction_count;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
