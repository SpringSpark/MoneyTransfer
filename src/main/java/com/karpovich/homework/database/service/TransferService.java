package com.karpovich.homework.database.service;

import com.karpovich.homework.database.DAO.AccountDao;
import com.karpovich.homework.database.DAO.TransactionDao;
import com.karpovich.homework.database.DAO.TransferDao;
import com.karpovich.homework.exceptions.DataValidationException;
import com.karpovich.homework.exceptions.DatabaseException;
import com.karpovich.homework.model.Account;
import com.karpovich.homework.model.Transfer;

public class TransferService {
    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final TransactionDao transactionDao;

    public TransferService(TransferDao transferDao, AccountDao accountDao, TransactionDao transactionDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    public void createNewTransfer(Transfer transfer) throws DataValidationException, DatabaseException {
        if (transfer.getSenderAccount() < 0 || transfer.getReceiverAccount() < 0 || transfer.getAmount() < 0 || transfer.getSenderAccount() == transfer.getReceiverAccount()) {
            throw new DataValidationException("Invalid transfer data");
        }
        try {
            transactionDao.startTransaction();

            Account senderAccount = accountDao.get(transfer.getSenderAccount());
            Account receiverAccount = accountDao.get(transfer.getReceiverAccount());
            if (senderAccount == null || receiverAccount == null) {
                throw new DataValidationException("Invalid account number for transfer.");
            }
            if (senderAccount.getAmount() < transfer.getAmount()) {
                throw new DataValidationException("Sender account does not have enough money.");
            }

            senderAccount.setAmount(senderAccount.getAmount() - transfer.getAmount());
            receiverAccount.setAmount(receiverAccount.getAmount() + transfer.getAmount());

            accountDao.save(senderAccount);
            accountDao.save(receiverAccount);
            transferDao.save(transfer);

        } finally {
            transactionDao.finishTransaction();
        }
    }

    public Transfer getTransfer(long id) throws DataValidationException {
        if (id < 0) {
            throw new DataValidationException("Negative transfer id");
        }
        transactionDao.startTransaction();
        Transfer transfer = transferDao.get(id);
        transactionDao.finishTransaction();

        if (transfer == null) {
            throw new DataValidationException("Invalid transfer id");
        }
        return transfer;
    }
}
