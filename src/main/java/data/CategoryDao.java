package data;

import model.Category;

import javax.sql.DataSource;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CategoryDao {

    private final DataSource dataSource;

    public CategoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
        initTable();
    }

    public void create(Category category) {
        if (category.getId() != null){
            throw new IllegalArgumentException(String.format("Category already has ID: %s", category));
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO CATEGORY (NAME, COLOR) VALUES (?, ?)",
                     RETURN_GENERATED_KEYS)) {
            st.setString(1, category.getName());
            st.setString(2, category.getColor().toString());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    category.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for category: " + category);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store category " + category, ex);
        }
    }

    public void delete(Category category) {
        if (category.getId() == null){
            throw new IllegalArgumentException("Category has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "DELETE FROM CATEGORY WHERE ID = ?"
             )){
            st.setLong(1, category.getId());
            int updatedRowCount = st.executeUpdate();
            if(updatedRowCount == 0){
                throw new DataAccessException("Failed to delete non-existing category: " + category);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete category " + category, ex);
        }
    }

    public void update(Category category) {
        if (category.getId() == null){
            throw new IllegalArgumentException("Category has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE CATEGORY SET NAME = ?, COLOR = ? WHERE ID = ?"
             )){
            st.setString(1, category.getName());
            st.setString(2, category.getColor();
            st.setLong(5, category.getId());
            int updatedRowCount = st.executeUpdate();
            if(updatedRowCount == 0){
                throw new DataAccessException("Failed to update non-existing category: " + category);
            }
        }  catch (SQLException ex)
        {
            throw new DataAccessException("Failed to update category " + category, ex);
        }
    }

    public List<Category> findAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, NAME, COLOR FROM CATEGORY")) {
            List<Category> categories = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(
                            rs.getString("NAME"),
                            Color.decode(rs.getString("COLOR")));
                    category.setId(rs.getLong("ID"));
                    categories.add(category);
                }
            }
            return categories;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all categories", ex);
        }
    }

    private void initTable() {
        if (!tableExits("APP", "CATEGORY")) {
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

            st.executeUpdate("CREATE TABLE APP.CATEGORY (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "COLOR VARCHAR(100) NOT NULL," +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create CATEGORY table", ex);
        }
    }

    public void dropTable() {
        try (var connection = dataSource.getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.CATEGORY");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop CATEGORY table", ex);
        }
    }
}
