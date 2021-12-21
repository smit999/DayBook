package com.daybook.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daybook.db.model.CategoryModel;
import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CategoryModel categoryModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCategory(List<CategoryModel> categoryList);

    @Query("DELETE FROM categories")
    void deleteAllCategory();

    @Query("SELECT * from categories where category_type = :type")
    List<CategoryModel> getAllCategoriesByType(int type);

    @Query("SELECT * from categories")
    List<CategoryModel> getAllCategories();

    @Query("DELETE from categories where id = :categoryId")
    void deleteCategory(int categoryId);
}
