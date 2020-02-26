package com.karpovich.homework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karpovich.homework.database.DAO.AccountDao;
import com.karpovich.homework.database.DAO.TransactionDao;
import com.karpovich.homework.database.DAO.TransferDao;
import com.karpovich.homework.database.Database;
import com.karpovich.homework.database.service.AccountService;
import com.karpovich.homework.database.service.TransferService;

public class ApplicationConfiguration {
    private static final Database DATABASE = Database.getInstance();
    private static final AccountDao ACCOUNT_DAO = new AccountDao(DATABASE.getSession());
    private static final TransferDao TRANSFER_DAO = new TransferDao(DATABASE.getSession());
    private static final TransactionDao TRANSACTION_DAO = new TransactionDao(DATABASE.getSession());
    private static final AccountService ACCOUNT_SERVICE = new AccountService(ACCOUNT_DAO, TRANSACTION_DAO);
    private static final TransferService TRANSFER_SERVICE = new TransferService(TRANSFER_DAO, ACCOUNT_DAO, TRANSACTION_DAO);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    public static AccountService getAccountService() {
        return ACCOUNT_SERVICE;
    }

    public static TransferService getTransferService() {
        return TRANSFER_SERVICE;
    }
}
