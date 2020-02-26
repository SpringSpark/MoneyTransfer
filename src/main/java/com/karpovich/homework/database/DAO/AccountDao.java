package com.karpovich.homework.database.DAO;

import com.karpovich.homework.database.Database;
import com.karpovich.homework.exceptions.DatabaseException;
import com.karpovich.homework.model.Account;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.DataException;

import java.util.List;

public class AccountDao implements Dao<Account> {

    private final Session session;

    public AccountDao(Session session) {
        this.session = session;
    }

    @Override
    public Account get(long id) {
        Account account;
        try {
            account = session.get(Account.class, id);
            Hibernate.initialize(account);

        } catch (HibernateException e) {
            System.out.println("Account with id " + id + " is not found:" + e.getMessage());
            account = null;
        }
        return account;
    }

    @Override
    public void save(Account account) throws DatabaseException {
        try {
            session.save(account);
        } catch (HibernateException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
