package data;

public class StatisticDao {
    private final CategoryDao categoryDao;
    private final TransactionDao transactionDao;

    public StatisticDao(CategoryDao categoryDao, TransactionDao transactionDao){
        this.categoryDao = categoryDao;
        this.transactionDao = transactionDao;
    }
}
