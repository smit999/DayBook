package com.daybook.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.daybook.db.model.DashboardItem;
import com.daybook.db.model.TransactionModel;

import java.util.Date;
import java.util.List;

@Dao
public interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TransactionModel transactionModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TransactionModel> transactionModels);

    @Update
    void updateTransaction(TransactionModel transactionModel);

    @Query("DELETE FROM transactions where transaction_id = :transactionId")
    void deleteTransaction(int transactionId);

    @Query("SELECT * from transactions where time_stamp BETWEEN :startDate AND :endDate and category_id = :categoryId")
    List<TransactionModel> getTransactions(long startDate, long endDate, int categoryId);

    @Query("SELECT * from transactions where time_stamp BETWEEN :startDate AND :endDate")
    List<TransactionModel> getTransactions(long startDate, long endDate);

    @Query("SELECT * from transactions where transaction_type = :transactionType AND time_stamp BETWEEN :startDate AND :endDate")
    List<TransactionModel> getTransactionsByType(int transactionType, long startDate, long endDate);

    @Query("SELECT * from transactions where transaction_type = :transactionType AND time_stamp BETWEEN :startDate AND :endDate and category_id = :categoryId")
    List<TransactionModel> getTransactionsByType(int transactionType, long startDate, long endDate, int categoryId);

    @Query("SELECT * from transactions where category_id = :categoryId AND time_stamp BETWEEN :startDate AND :endDate")
    List<TransactionModel> getSubTransaction(long startDate, long endDate, int categoryId);

    @Query("SELECT (SELECT sum(t.amount) from transactions t where t.transaction_type = 1 and time_stamp BETWEEN :startDate AND :endDate) as expense, " +
            "(SELECT sum(t.amount) from transactions t where t.transaction_type = 2 and time_stamp BETWEEN :startDate AND :endDate) as income" +
            ",count(t.transaction_id) as transaction_count,sum(t.amount) AS total,t.category_name,t.category_color,t.category_icon ,t.category_id from transactions t " +
            "inner join categories c on t.category_id = c.id " +
            "where time_stamp BETWEEN :startDate AND :endDate GROUP by t.category_id Order by total DESC")
    List<DashboardItem> getDashBoardTransactions(long startDate, long endDate);

    @Query("SELECT * from transactions")
    List<TransactionModel> getAllTransaction();

    @Query("SELECT category_id from transactions where category_id = :categoryId")
    int checkCategoryInTransaction(int categoryId);

}