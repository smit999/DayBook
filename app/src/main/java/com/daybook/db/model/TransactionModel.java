package com.daybook.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class TransactionModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    private int transactionId;

    @ColumnInfo(name = "transaction_info")
    private String transactionInfo;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "transaction_type")
    private int transactionType;

    @ColumnInfo(name = "category_name")
    private String categoryName;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "time_stamp")
    private long timeStamp;

    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "category_color")
    private String categoryColor;

    @ColumnInfo(name = "category_icon")
    private String categoryIcon;

    @ColumnInfo(name = "paid_by")
    private String paidBy;

    @ColumnInfo(name = "attachment_image")
    private String attachmentImage;

    @ColumnInfo(name = "misc")
    private String misc;

    public TransactionModel() {

    }

    protected TransactionModel(Parcel in) {
        transactionId = in.readInt();
        transactionInfo = in.readString();
        amount = in.readDouble();
        transactionType = in.readInt();
        categoryName = in.readString();
        categoryId = in.readInt();
        timeStamp = in.readLong();
        subject = in.readString();
        categoryColor = in.readString();
        categoryIcon = in.readString();
        paidBy = in.readString();
        attachmentImage = in.readString();
        misc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(transactionId);
        dest.writeString(transactionInfo);
        dest.writeDouble(amount);
        dest.writeInt(transactionType);
        dest.writeString(categoryName);
        dest.writeInt(categoryId);
        dest.writeLong(timeStamp);
        dest.writeString(subject);
        dest.writeString(categoryColor);
        dest.writeString(categoryIcon);
        dest.writeString(paidBy);
        dest.writeString(attachmentImage);
        dest.writeString(misc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionModel> CREATOR = new Creator<TransactionModel>() {
        @Override
        public TransactionModel createFromParcel(Parcel in) {
            return new TransactionModel(in);
        }

        @Override
        public TransactionModel[] newArray(int size) {
            return new TransactionModel[size];
        }
    };

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(String transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }


    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }


    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getAttachmentImage() {
        return attachmentImage;
    }

    public void setAttachmentImage(String attachmentImage) {
        this.attachmentImage = attachmentImage;
    }

    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }
}