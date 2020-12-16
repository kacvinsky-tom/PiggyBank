package data;

import model.Category;
import model.CategoryStatistic;
import model.Transaction;
import model.TransactionType;

import javax.sql.DataSource;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticDao {
    private final DataSource dataSource;

    public StatisticDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public List<CategoryStatistic> setAll (){
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, \"NAME\", COLOR FROM CATEGORIES")) {
            List<CategoryStatistic> categoryStatistics = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(rs.getString("NAME"), new Color(10,10,10));
                    category.setId(rs.getLong("ID"));
                    CategoryStatistic categoryStatistic = new CategoryStatistic(category);
                    categoryStatistic.setTransactionsNumber(getNumberOfTransactions(category));
                    categoryStatistic.setExpenses(getIncomeExpense(category, TransactionType.SPENDING));
                    categoryStatistic.setIncome(getIncomeExpense(category, TransactionType.INCOME));
                    categoryStatistic.setSum(categoryStatistic.getIncome() - categoryStatistic.getExpenses());
                    categoryStatistic.setPercentageInc(categoryStatistic.getIncome() / getTotalIncomeExpense(TransactionType.INCOME));
                    categoryStatistic.setPercentageSpend(categoryStatistic.getExpenses() / getTotalIncomeExpense(TransactionType.SPENDING));
                    categoryStatistics.add(categoryStatistic);
                }
            }
            return categoryStatistics;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to set all CategoryStatistics", ex);
        }

    }

    public int getNumberOfTransactions(Category category){
        int all = 0;
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT COUNT(*) AS total FROM TRANSACTIONS WHERE CATEGORY_ID = ?"
             )){
            st.setLong(1, category.getId());
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    all = (int) rs.getLong("total");
                }
            }
            return all;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to find category " + category, ex);
        }
    }

    public long getIncomeExpense(Category category, TransactionType transactionType){
        long all = 0;
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT SUM(AMOUNT) AS totalAmount FROM TRANSACTIONS WHERE CATEGORY_ID = ? AND \"TYPE\" = ?"
             )){
            st.setLong(1, category.getId());
            st.setString(2, transactionType.name());
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    all = rs.getLong("totalAmount");
                }
            }
            return all;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to find category " + category, ex);
        }
    }

    public long getTotalIncomeExpense(TransactionType transactionType){
        long all = 0;
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT SUM(AMOUNT) AS totalIncome FROM TRANSACTIONS WHERE \"TYPE\" = ?"
             )){
            st.setString(1, transactionType.name());
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    all = rs.getLong("totalIncome");
                }
            }
            return all;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to find category " + ex);
        }
    }
}
