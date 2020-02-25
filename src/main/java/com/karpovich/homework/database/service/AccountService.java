package com.karpovich.homework.database.service;

import com.karpovich.homework.database.DAO.AccountDao;
import com.karpovich.homework.exceptions.DataValidationException;
import com.karpovich.homework.model.Account;

import java.util.List;

public class AccountService {
    private AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void createNewAccount(Account account) throws DataValidationException {

    }

    public Account getAccount(int number) {
        Account account = new Account(11, 22);

        return account;
    }

    public List<Account> getAllAccounts() {
        return accountDao.getAll();
    }

}
