package com.karpovich.homework;

import com.karpovich.homework.api.AccountHandler;
import com.karpovich.homework.api.TransferHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.InetSocketAddress;

import static com.karpovich.homework.ApplicationConfiguration.getAccountService;
import static com.karpovich.homework.ApplicationConfiguration.getObjectMapper;
import static com.karpovich.homework.ApplicationConfiguration.getTransferService;

public class Application {


    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("log4j.properties");
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        AccountHandler accountHandler = new AccountHandler(getAccountService(), getObjectMapper());
        TransferHandler transferHandler = new TransferHandler(getTransferService(), getObjectMapper());

        server.createContext("/api/account/create", accountHandler::handleCreate);
        server.createContext("/api/account/get", accountHandler::handleGet);

        server.createContext("/api/transfer/create", transferHandler::handleCreate);
        server.createContext("/api/transfer/get", transferHandler::handleGet);

        server.start();
    }
}
