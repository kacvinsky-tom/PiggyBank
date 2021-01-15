package data;

import enums.TransactionType;
import model.Category;
import model.Transaction;

import javax.sql.DataSource;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CategoryTransactionDao {

    private final DataSource dataSource;

    public CategoryTransactionDao(DataSource dataSource){
        this.dataSource = dataSource;
        initTable();
    }

    public void create(Transaction transaction, Category category) {
        if (transaction.getId() == null){
            throw new IllegalArgumentException("Transaction has null ID");
        }
        if (category.getId() == null){
            throw new IllegalArgumentException("Category has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO TRANSACTIONS_CATEGORIES (CATEGORY_ID, TRANSACTION_ID) VALUES (?, ?)",
                     RETURN_GENERATED_KEYS)) {
            st.setLong(1, transaction.getId());
            st.setLong(2, category.getId());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    transaction.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for transaction: " + transaction);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store transaction " + transaction, ex);
        }
    }

    public void deleteOnlyOneRecord(Transaction transaction, Category category) {
        if (transaction.getId() == null){
            throw new IllegalArgumentException("Transaction has null ID");
        }
        if (category.getId() == null){
            throw new IllegalArgumentException("Category has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "DELETE FROM TRANSACTIONS_CATEGORIES WHERE TRANSACTION_ID = ? AND CATEGORY_ID = ?"
             )){
            st.setLong(1, transaction.getId());
            st.setLong(2, category.getId());
            int updatedRowCount = st.executeUpdate();
            if(updatedRowCount == 0){
                throw new DataAccessException("Failed to delete non-existing transaction or category: " + transaction);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete transaction and category " + transaction, ex);
        }
    }

    public void delete(Transaction transaction) {
        if (transaction.getId() == null){
            throw new IllegalArgumentException("Transaction has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "DELETE FROM TRANSACTIONS_CATEGORIES WHERE TRANSACTION_ID = ?"
             )){
            st.setLong(1, transaction.getId());
            int updatedRowCount = st.executeUpdate();
            if(updatedRowCount == 0){
                throw new DataAccessException("Failed to delete non-existing transaction: " + transaction);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete transaction " + transaction, ex);
        }
    }

    public List<Transaction> findAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT TRANSACTIONS.ID AS TRANS_ID, AMOUNT, \"TYPE\", TRANSACTIONS.NAME AS TRANS_NAME, " +
                             "CREATION_DATE, NOTE, CATEGORY_ID, CATEGORIES.NAME AS CAT_NAME, COLOR" +
                             " FROM TRANSACTIONS_CATEGORIES LEFT OUTER JOIN TRANSACTIONS ON TRANSACTIONS.ID = TRANSACTIONS_CATEGORIES.TRANSACTION_ID" +
                             " LEFT OUTER JOIN CATEGORIES ON CATEGORIES.ID = TRANSACTIONS_CATEGORIES.CATEGORY_ID"))
        {
            List<Transaction> transactions = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    var Id =  rs.getLong("TRANS_ID");
                    Transaction trans = transactions.stream().filter(e -> e.getId() == Id).findAny().orElse(null);
                    if (trans != null){
                        trans.getCategories().add(new Category(rs.getString("CAT_NAME"),
                                Color.decode(rs.getString("COLOR"))));
                        continue;
                    }
                    TransactionType type = TransactionType.valueOf(rs.getString("TYPE"));
                    BigDecimal amount = BigDecimal.valueOf(rs.getDouble("AMOUNT"));
                    List<Category> list = new ArrayList<>();
                    list.add(new Category(rs.getString("CAT_NAME"), Color.decode(rs.getString("COLOR"))));
                    Transaction transaction = new Transaction(
                            rs.getString("TRANS_NAME"),
                            amount,
                            list,
                            new java.util.Date(rs.getDate("CREATION_DATE").getTime()),
                            rs.getString("NOTE"),
                            type);
                    transaction.setId(rs.getLong("TRANS_ID"));
                    transactions.add(transaction);
                }
            }
            return transactions;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all transactions with categories", ex);
        }
    }

    private void initTable() {
        if (!tableExits("APP", "TRANSACTIONS_CATEGORIES")) {
            createTable();
        }
    }

    private boolean tableExits(String schemaName, String tableName) {
        try (var connection = dataSource.getConnection();
             var rs = connection.getMetaData().getTables(null, schemaName, tableName, null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + schemaName + "." + tableName + " exist", ex);
        }
    }

    private void createTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.TRANSACTIONS_CATEGORIES (" +
                    "TRANSACTION_ID BIGINT REFERENCES APP.TRANSACTIONS(ID)," +
                    "CATEGORY_ID BIGINT DEFAULT 0 REFERENCES APP.CATEGORIES(ID) ON DELETE SET DEFAULT" +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create TRANSACTIONS_CATEGORIES table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.TRANSACTIONS_CATEGORIES");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop TRANSACTIONS_CATEGORIES table", ex);
        }
    }
}
