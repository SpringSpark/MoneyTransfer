package com.karpovich.homework.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.karpovich.homework.database.service.TransferService;
import com.karpovich.homework.exceptions.DataValidationException;
import com.karpovich.homework.exceptions.DatabaseException;
import com.karpovich.homework.model.Transfer;
import com.sun.net.httpserver.HttpExchange;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static com.karpovich.homework.api.Handler.splitQuery;

public class TransferHandler {

    private TransferService transferService;
    private ObjectMapper mapper;

    final static Logger logger = Logger.getLogger(AccountHandler.class);

    public TransferHandler(TransferService transferService, ObjectMapper mapper) {
        this.transferService = transferService;
        this.mapper = mapper;
    }

    //localhost:8000/api/transfer/create?sender=1&receiver=2&amount=100
    public void handleCreate(HttpExchange exchange) throws IOException {
        logger.debug("Received new request: " + exchange.getRequestURI());

        String responseMessage;
        ResponseCode responseCode;
        if ("POST".equals(exchange.getRequestMethod())) {

            Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());

            try {
                if (!params.containsKey("sender") || !params.containsKey("receiver") || !params.containsKey("amount")) {
                    throw new DataValidationException("Invalid request");
                }

                long senderId = params.get("sender").stream().map(Long::valueOf).findFirst().orElseThrow(() -> new DataValidationException("No sender id was provided"));
                long receiverId = params.get("receiver").stream().map(Long::valueOf).findFirst().orElseThrow(() -> new DataValidationException("No sender id was provided"));
                long amount = params.get("amount").stream().map(Long::valueOf).findFirst().orElseThrow(() -> new DataValidationException("No amount was provided"));

                Transfer transfer = new Transfer(senderId, receiverId, amount);
                transferService.createNewTransfer(transfer);

                responseMessage = mapper.writeValueAsString(transfer);
                responseCode = ResponseCode.SUCCESS;

                logger.debug("Successfully created new transfer: " + transfer.toString());
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

    //localhost:8000/api/transfer/get?id=1
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

                Transfer transfer;
                transfer = transferService.getTransfer(id);

                responseMessage = mapper.writeValueAsString(transfer);
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
