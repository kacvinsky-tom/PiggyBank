
import data.CategoryDao;
import data.TransactionDao;
import ui.MainWindow;

import javax.sql.DataSource;
import javax.swing.UIManager;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.EmbeddedDataSource;

public class Main {

    public static void main(String[] args) {
        CategoryDao categoryDao = new CategoryDao(createDataSource());
        TransactionDao transactionDao = new TransactionDao(createDataSource());
        initNimbusLookAndFeel();
        EventQueue.invokeLater(() -> new MainWindow(categoryDao, transactionDao).show());
    }

    private static DataSource createDataSource() {
        String dbPath = System.getProperty("user.home") + "/employee-evidence";
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(dbPath);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }

    private static void initNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Nimbus layout initialization failed", ex);
        }
    }
}
