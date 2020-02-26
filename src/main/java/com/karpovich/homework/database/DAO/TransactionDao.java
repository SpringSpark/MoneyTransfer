package com.karpovich.homework.database.DAO;

import com.karpovich.homework.database.Database;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

public class TransactionDao {
    private final Session session;

    public TransactionDao(Session session) {
        this.session = session;
    }

    public void startTransaction() {
        session.getTransaction().begin();
    }

    public void finishTransaction() {
        Transaction transaction = session.getTransaction();
        if (TransactionStatus.MARKED_ROLLBACK == transaction.getStatus()) {
            transaction.rollback();
        }
        if (TransactionStatus.ACTIVE == transaction.getStatus()) {
            transaction.commit();
        }
    }
}
