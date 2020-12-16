package data;

import enums.TransactionType;
import model.Category;
import model.CategoryStatistic;

import javax.sql.DataSource;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
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
                    categoryStatistic.setSum(categoryStatistic.getIncome().subtract(categoryStatistic.getExpenses()) );
                    setIncomeAndExpense(categoryStatistic);
                    categoryStatistics.add(categoryStatistic);
                }
                return categoryStatistics;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to set all CategoryStatistics", ex);
        }
    }

    private void setIncomeAndExpense(CategoryStatistic categoryStatistic) {
        try {
            categoryStatistic.setPercentageInc((categoryStatistic.getIncome()
                    .divide(getTotalIncomeExpense(TransactionType.INCOME),2,RoundingMode.HALF_UP))
                    .multiply(new BigDecimal(100)));
        } catch (ArithmeticException e){
            categoryStatistic.setPercentageInc(new BigDecimal(0));
        }
        try {
            categoryStatistic.setPercentageSpend((categoryStatistic.getExpenses()
                    .divide(getTotalIncomeExpense(TransactionType.SPENDING),2,RoundingMode.HALF_UP))
                    .multiply(new BigDecimal(100)));
        } catch (ArithmeticException e){
            categoryStatistic.setPercentageSpend(new BigDecimal(0));
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
            throw new DataAccessException("Failed to get number of transactions " + category, ex);
        }
    }

    public BigDecimal getIncomeExpense(Category category, TransactionType transactionType){
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
            return BigDecimal.valueOf(all);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get income or expense " + category, ex);
        }
    }

    public BigDecimal getTotalIncomeExpense(TransactionType transactionType){
        long all = 0;
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT SUM(AMOUNT) AS total FROM TRANSACTIONS WHERE \"TYPE\" = ?"
             )){
            st.setString(1, transactionType.name());
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    all = rs.getLong("total");
                }
            }
            return BigDecimal.valueOf(all);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get total income or expense " + ex);
        }
    }

    public BigDecimal getIncome(){
        long balance;
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT SUM(AMOUNT) AS total FROM TRANSACTIONS WHERE \"TYPE\" = ?"
             , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
            st.setString(1, TransactionType.INCOME.name());
            try (var rs = st.executeQuery()) {
                rs.first();
                balance = rs.getLong("total");
            } catch (SQLException ex){
                return BigDecimal.valueOf(0);
            }
            return BigDecimal.valueOf(balance);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get Income " + ex);
        }
    }

    public BigDecimal getExpense(){
        long balance;
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT SUM(AMOUNT) AS total FROM TRANSACTIONS WHERE \"TYPE\" = ?"
                     , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
            st.setString(1, TransactionType.SPENDING.name());
            try (var rs = st.executeQuery()) {
                rs.first();
                balance = rs.getLong("total");
            } catch (SQLException ex){
                return BigDecimal.valueOf(0);
            }
            return BigDecimal.valueOf(balance);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get Income " + ex);
        }
    }
}
