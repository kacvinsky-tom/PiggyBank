package data;

import model.Category;
import model.Transaction;
import enums.TransactionType;

import javax.sql.DataSource;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class TransactionDao {

    private final DataSource dataSource;

    public TransactionDao(DataSource dataSource) {
        this.dataSource = dataSource;
        initTable();
    }

    public void create(Transaction transaction) {
        if (transaction.getId() != null){
            throw new IllegalArgumentException(String.format("Transaction already has ID: %s", transaction));
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO TRANSACTIONS (AMOUNT, \"TYPE\", \"NAME\", CREATION_DATE, NOTE) VALUES (?, ?, ?, ?, ?)",
                     RETURN_GENERATED_KEYS)) {
            setStatementParameters(transaction, st);
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

    private void setStatementParameters(Transaction transaction, PreparedStatement st) throws SQLException {
        st.setBigDecimal(1, transaction.getAmount());
        st.setString(2, transaction.getType().name());
        st.setString(3, transaction.getName());
        st.setDate(4, new Date(transaction.getDate().getTime()));
        st.setString(5, transaction.getNote());
    }

    public void delete(Transaction transaction) {
        if (transaction.getId() == null){
            throw new IllegalArgumentException("Transaction has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "DELETE FROM TRANSACTIONS WHERE ID = ?"
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

    public void update(Transaction transaction) {
        if (transaction.getId() == null){
            throw new IllegalArgumentException("Transaction has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE TRANSACTIONS SET AMOUNT = ?, \"TYPE\" = ?, \"NAME\" = ?, CREATION_DATE = ?, NOTE = ? WHERE ID = ?"
             )){
            setStatementParameters(transaction, st);
            st.setLong(7, transaction.getId());
            int updatedRowCount = st.executeUpdate();
            if(updatedRowCount == 0){
                throw new DataAccessException("Failed to update non-existing transaction: " + transaction);
            }
        }  catch (SQLException ex)
        {
            throw new DataAccessException("Failed to update transaction " + transaction, ex);
        }
    }

    private void initTable() {
        if (!tableExits("APP", "TRANSACTIONS")) {
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
            st.executeUpdate("CREATE TABLE APP.TRANSACTIONS (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "AMOUNT DECIMAL(30,2) NOT NULL," +
                    "\"TYPE\" VARCHAR(8) NOT NULL CONSTRAINT TYPE_CHECK CHECK (\"TYPE\" IN ('INCOME','SPENDING'))," +
                    "\"NAME\" VARCHAR(100) NOT NULL," +
                    "CREATION_DATE DATE NOT NULL," +
                    "NOTE VARCHAR(255)," +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create TRANSACTIONS table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.TRANSACTIONS");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop TRANSACTIONS table", ex);
        }
    }
}
