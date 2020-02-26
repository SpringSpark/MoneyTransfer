import com.fasterxml.jackson.databind.ObjectMapper;
import com.karpovich.homework.api.AccountHandler;
import com.karpovich.homework.api.ResponseCode;
import com.karpovich.homework.database.service.AccountService;
import com.karpovich.homework.exceptions.DataValidationException;
import com.karpovich.homework.model.Account;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountHandlerTest {

    private AccountService accountService = mock(AccountService.class);
    private ObjectMapper objectMapper = mock(ObjectMapper.class);

    private AccountHandler accountHandler = new AccountHandler(accountService, objectMapper);

    @Test
    public void handleCreateTest() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("POST");
        URI uri = URI.create("/api/account/create?id=1&balance=2000");

        when(httpExchange.getRequestURI()).thenReturn(uri);
        OutputStream outputStream = mock(OutputStream.class);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        Account account = new Account(1, 100);
        when( objectMapper.writeValueAsString(any(Account.class))).thenReturn(account.toString());

        accountHandler.handleCreate(httpExchange);

        verify(httpExchange, Mockito.times(1)).sendResponseHeaders(ResponseCode.SUCCESS.getCode(), account.toString().length());
    }

    @Test
    public void handleCreateIncorrectRequestTypeTest() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        OutputStream outputStream = mock(OutputStream.class);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        accountHandler.handleCreate(httpExchange);

        verify(httpExchange, Mockito.times(1)).sendResponseHeaders(ResponseCode.METHOD_NOT_ALLOWED.getCode(), 0);
    }

    @Test
    public void handleCreateIncorrectRequestNoIdTest() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("POST");
        URI uri = URI.create("/api/account/create?balance=2000");

        when(httpExchange.getRequestURI()).thenReturn(uri);
        OutputStream outputStream = mock(OutputStream.class);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        accountHandler.handleCreate(httpExchange);

        verify(httpExchange, Mockito.times(1)).sendResponseHeaders(ResponseCode.INVALID_REQUEST.getCode(), "Invalid request".length());
    }

    @Test
    public void handleCreateIncorrectRequestNoBalanceTest() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("POST");
        URI uri = URI.create("/api/account/create?id=1");

        when(httpExchange.getRequestURI()).thenReturn(uri);
        OutputStream outputStream = mock(OutputStream.class);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        accountHandler.handleCreate(httpExchange);

        verify(httpExchange, Mockito.times(1)).sendResponseHeaders(ResponseCode.INVALID_REQUEST.getCode(), "Invalid request".length());
    }

    @Test
    public void handleCreateIncorrectRequestNumberFormatTest() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("POST");
        URI uri = URI.create("/api/account/create?id=am&balance=2000");

        when(httpExchange.getRequestURI()).thenReturn(uri);
        OutputStream outputStream = mock(OutputStream.class);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        accountHandler.handleCreate(httpExchange);

        verify(httpExchange, Mockito.times(1)).sendResponseHeaders(ResponseCode.INVALID_REQUEST.getCode(), "For input string: \"am\"".length());
    }

    @Test
    public void handleGetTest() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        URI uri = URI.create("/api/account/get?id=1");

        when(httpExchange.getRequestURI()).thenReturn(uri);
        OutputStream outputStream = mock(OutputStream.class);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        Account account = new Account(1, 100);
        when( objectMapper.writeValueAsString(any(Account.class))).thenReturn(account.toString());

        accountHandler.handleGet(httpExchange);

        verify(httpExchange, Mockito.times(1)).sendResponseHeaders(ResponseCode.SUCCESS.getCode(), account.toString().length());
    }

    @Test
    public void handleGetIncorrectRequestTypeTest() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("POST");
        OutputStream outputStream = mock(OutputStream.class);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        accountHandler.handleGet(httpExchange);

        verify(httpExchange, Mockito.times(1)).sendResponseHeaders(ResponseCode.METHOD_NOT_ALLOWED.getCode(), 0);
    }

    @Test
    public void handleGetIncorrectRequestNoIdTest() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        URI uri = URI.create("/api/account/get");

        when(httpExchange.getRequestURI()).thenReturn(uri);
        OutputStream outputStream = mock(OutputStream.class);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        accountHandler.handleGet(httpExchange);

        verify(httpExchange, Mockito.times(1)).sendResponseHeaders(ResponseCode.INVALID_REQUEST.getCode(), "Invalid request".length());
    }

    @Test
    public void handleGetIncorrectRequestNumberFormatTest() throws IOException {
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        URI uri = URI.create("/api/account/get?id=uuu");

        when(httpExchange.getRequestURI()).thenReturn(uri);
        OutputStream outputStream = mock(OutputStream.class);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        accountHandler.handleGet(httpExchange);

        verify(httpExchange, Mockito.times(1)).sendResponseHeaders(ResponseCode.INVALID_REQUEST.getCode(), "For input string: \"uuu\"".length());
    }

}
