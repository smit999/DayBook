package com.daybook.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "categories")
public class CategoryModel implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "category_name")
    private String categoryName;
    @ColumnInfo(name = "category_icon")
    private String categoryIcon;

    @ColumnInfo(name = "category_type")
    private int categoryType;
    @ColumnInfo(name = "is_system")
    private boolean isSystem;
    @ColumnInfo(name = "created")
    private long created;
    @ColumnInfo(name = "updated")
    private long updated;
    @ColumnInfo(name = "category_color")
    private String categoryColor;

    public CategoryModel(){

    }
    protected CategoryModel(Parcel in) {
        id = in.readInt();
        categoryName = in.readString();
        categoryIcon = in.readString();
        categoryType = in.readInt();
        isSystem = in.readByte() != 0;
        created = in.readLong();
        updated = in.readLong();
        categoryColor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(categoryName);
        dest.writeString(categoryIcon);
        dest.writeInt(categoryType);
        dest.writeByte((byte) (isSystem ? 1 : 0));
        dest.writeLong(created);
        dest.writeLong(updated);
        dest.writeString(categoryColor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }
}
