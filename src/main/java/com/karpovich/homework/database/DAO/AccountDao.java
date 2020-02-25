package com.karpovich.homework.database.DAO;

import com.karpovich.homework.model.Account;
import org.hibernate.SessionFactory;

import java.util.List;

public class AccountDao implements Dao<Account> {


    @Override
    public Account get(long id) {
        return null;
    }

    @Override
    public List<Account> getAll() {
        //return list(namedQuery("model.Account.findAll"));
        return null;
    }

    @Override
    public void save(Account account) {

    }

    @Override
    public void update(Account account, String[] params) {

    }

    @Override
    public void delete(Account account) {

    }
}
