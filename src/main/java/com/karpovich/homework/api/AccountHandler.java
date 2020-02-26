package com.karpovich.homework.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karpovich.homework.database.service.AccountService;
import com.karpovich.homework.exceptions.DataValidationException;
import com.karpovich.homework.exceptions.DatabaseException;
import com.karpovich.homework.model.Account;
import com.sun.net.httpserver.HttpExchange;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class AccountHandler extends Handler {

    private AccountService accountService;
    private ObjectMapper mapper;

    public AccountHandler(AccountService accountService, ObjectMapper mapper) {
        this.accountService = accountService;
        this.mapper = mapper;
    }

    final static Logger logger = Logger.getLogger(AccountHandler.class);

    //localhost:8000/api/account/create?id=1&balance=100
    public void handleCreate(HttpExchange exchange) throws IOException {
        logger.debug("Received new request: " + exchange.getRequestURI());

        String responseMessage;
        ResponseCode responseCode;
        if ("POST".equals(exchange.getRequestMethod())) {

            Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());

            try {
                if (!params.containsKey("id") || !params.containsKey("balance")) {
                    throw new DataValidationException("Invalid request");
                }

                long id = params.get("id").stream().map(Long::valueOf).findFirst().orElseThrow(() -> new DataValidationException("No id was provided"));
                long balance = params.get("balance").stream().map(Long::valueOf).findFirst().orElseThrow(() -> new DataValidationException("No balance was provided"));

                Account account = new Account(id, balance);
                accountService.createNewAccount(account);

                responseMessage = mapper.writeValueAsString(account);
                responseCode = ResponseCode.SUCCESS;

                logger.debug("Successfully created new account: " + account.toString());
            } catch (DataValidationException | NumberFormatException e) {
                responseMessage = e.getMessage();
                responseCode = ResponseCode.INVALID_REQUEST;

                logger.debug("Error processing request: " + responseMessage);
            } catch (DatabaseException e) {
                responseMessage = e.getMessage();
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR;

                logger.debug("Internal database error: " + responseMessage);
            }

        } else {
            responseMessage = "";
            responseCode = ResponseCode.METHOD_NOT_ALLOWED;
        }

        exchange.sendResponseHeaders(responseCode.getCode(), responseMessage.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(responseMessage.getBytes());
        output.flush();
        exchange.close();
    }

    //localhost:8000/api/account/get?id=1
    public void handleGet(HttpExchange exchange) throws IOException {
        logger.debug("Received new request: " + exchange.getRequestURI());

        String responseMessage;
        ResponseCode responseCode;
        if ("GET".equals(exchange.getRequestMethod())) {
            Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());

            try {
                if (!params.containsKey("id")) {
                    throw new DataValidationException("Invalid request");
                }
                long id = params.get("id").stream().map(Long::valueOf).findFirst().orElseThrow(() -> new DataValidationException("No id was provided"));

                Account account;
                account = accountService.getAccountBalance(id);

                responseMessage = mapper.writeValueAsString(account);
                responseCode = ResponseCode.SUCCESS;
            } catch (DataValidationException | NumberFormatException e) {
                responseMessage = e.getMessage();
                responseCode = ResponseCode.INVALID_REQUEST;

                logger.debug("Error processing request: " + responseMessage);
            }

        } else {
            responseMessage = "";
            responseCode = ResponseCode.METHOD_NOT_ALLOWED;
        }
        exchange.sendResponseHeaders(responseCode.getCode(), responseMessage.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(responseMessage.getBytes());
        output.flush();
        exchange.close();
    }
}
