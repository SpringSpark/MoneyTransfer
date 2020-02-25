package com.karpovich.homework.database.service;

import com.karpovich.homework.database.DAO.AccountDao;
import com.karpovich.homework.database.DAO.TransferDao;
import com.karpovich.homework.exceptions.DataValidationException;
import com.karpovich.homework.model.Account;
import com.karpovich.homework.model.Transfer;

import java.util.List;

public class TransferService {
    private AccountDao accountDao;
    private TransferDao transferDao;

    public TransferService(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    public void createNewTransfer(Transfer transfer) throws DataValidationException {
        Account senderAccount = accountDao.get(transfer.getSenderAccount());
        Account receiverAccount = accountDao.get(transfer.getReceiverAccount());
        if (senderAccount == null || receiverAccount == null) {
            throw new DataValidationException("Invalid account number for transfer.");
        }
        if (senderAccount.getAmount() < transfer.getAmount()) {
            throw new DataValidationException("Sender account does not have enough money.");
        }
    }

    public Transfer getTransfer(int id) {
        Transfer transfer = null;

        return transfer;
    }

    public List<Transfer> getAllTransfers() {
        return transferDao.getAll();
    }
}
