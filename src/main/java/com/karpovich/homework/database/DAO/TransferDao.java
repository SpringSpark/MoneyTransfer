package com.karpovich.homework.database.DAO;

import com.karpovich.homework.database.Database;
import com.karpovich.homework.exceptions.DatabaseException;
import com.karpovich.homework.model.Transfer;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class TransferDao implements Dao<Transfer> {

    private final Session session;

    public TransferDao(Session session) {
        this.session = session;
    }

    @Override
    public Transfer get(long id) {
        Transfer transfer;
        try {
            transfer = session.get(Transfer.class, id);
            Hibernate.initialize(transfer);

        } catch (HibernateException e) {
            System.out.println("Transfer with id " + id + " is not found:" + e.getMessage());
            transfer = null;
        }
        return transfer;
    }

    @Override
    public void save(Transfer transfer) throws DatabaseException {
        try {
            session.save(transfer);
        } catch (HibernateException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

}