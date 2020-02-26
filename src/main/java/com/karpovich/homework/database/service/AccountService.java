package com.karpovich.homework.database.service;

import com.karpovich.homework.database.DAO.AccountDao;
import com.karpovich.homework.database.DAO.TransactionDao;
import com.karpovich.homework.exceptions.DataValidationException;
import com.karpovich.homework.exceptions.DatabaseException;
import com.karpovich.homework.model.Account;

import java.util.List;

public class AccountService {
    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    public AccountService(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    public void createNewAccount(Account account) throws DataValidationException, DatabaseException {
        if (account.getId() < 0 || account.getAmount() < 0) {
            throw new DataValidationException("Invalid account data");
        }

        transactionDao.startTransaction();
        Account accountFind = accountDao.get(account.getId());
        transactionDao.finishTransaction();

        if(accountFind != null) {
            throw new DataValidationException("Account with this id already exists");
        }

        transactionDao.startTransaction();
        accountDao.save(account);
        transactionDao.finishTransaction();
    }

    public Account getAccountBalance(long id) throws DataValidationException {
        if (id < 0) {
            throw new DataValidationException("Negative account id");
        }
        transactionDao.startTransaction();
        Account account = accountDao.get(id);
        transactionDao.finishTransaction();

        if (account == null) {
            throw new DataValidationException("Invalid account id");
        }

        return account;
    }

}
