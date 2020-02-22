package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exceptions.DataValidationException;
import model.Client;
import model.Transfer;
import org.h2.jdbcx.JdbcDataSource;

public class Database {

    private static final String URL = "jdbc:h2:mem:db";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";
    private static final String USER_TABLE = "USER";
    private static final String TRANSFER_TABLE = "TRANSFER";
    private static final String ACCOUNT_COLUMN = "ACCOUNT";
    private static final String SENDER_ACCOUNT_COLUMN = "SENDER_" + ACCOUNT_COLUMN;
    private static final String RECEIVER_ACCOUNT_COLUMN = "RECEIVER_" + ACCOUNT_COLUMN;
    private static final String AMOUNT_COLUMN = "AMOUNT";
    private static final String NAME_COLUMN = "NAME";
    private static final String SURNAME_COLUMN = "SURNAME";
    private static Connection connection;
    private static Database instance;

    private Database() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(URL);
        ds.setUser(USER);
        ds.setPassword(PASSWORD);
        try {
            connection = ds.getConnection();
            createStructure();
        } catch (SQLException e) {
            System.out.println("Database connection creation failed : " + e.getMessage());
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private void createStructure() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE " + USER_TABLE + " (" + ACCOUNT_COLUMN + " INT PRIMARY KEY, " + NAME_COLUMN + " VARCHAR(64) NOT NULL, " + SURNAME_COLUMN + " VARCHAR(64) NOT NULL);");
        statement.executeUpdate("CREATE TABLE " + TRANSFER_TABLE + " (" + SENDER_ACCOUNT_COLUMN + " INT NOT NULL, " + RECEIVER_ACCOUNT_COLUMN + " INT NOT NULL, " + AMOUNT_COLUMN + " INT NOT NULL);");
        statement.close();
        connection.commit();
    }

    public void createNewClient(Client client) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO " + USER_TABLE + " VALUES (?, ?, ?);");
        statement.setInt(1, client.getAccount());
        statement.setString(2, client.getName());
        statement.setString(3, client.getSurname());
        statement.execute();
        statement.close();
        connection.commit();
    }

    public Client findClientByAccount(int account) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + USER_TABLE + " WHERE " + ACCOUNT_COLUMN + " = " + account);
        if (!resultSet.next()) {
            return null;
        }
        resultSet.first();
        Client client = new Client(Integer.parseInt(resultSet.getString(ACCOUNT_COLUMN)), resultSet.getString(NAME_COLUMN), resultSet.getString("SUR" + NAME_COLUMN));
        return client;
    }

    public List<Client> getAllClients() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + USER_TABLE);
        List<Client> allClients = new ArrayList<>();
        while (resultSet.next()) {
            Client client = new Client(Integer.parseInt(resultSet.getString(ACCOUNT_COLUMN)), resultSet.getString(NAME_COLUMN), resultSet.getString("SUR" + NAME_COLUMN));
            allClients.add(client);
        }
        statement.close();
        return allClients;
    }

    public void createNewTransfer(Transfer transfer) throws SQLException {
        if (findClientByAccount(transfer.getSenderAccount()) == null || findClientByAccount(transfer.getReceiverAccount()) == null) {
            throw new DataValidationException("Invalid account number for transfer.");
        }
        PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TRANSFER_TABLE + "  VALUES (?, ?, ?);");
        statement.setInt(1, transfer.getSenderAccount());
        statement.setInt(2, transfer.getReceiverAccount());
        statement.setInt(3, transfer.getAmount());
        statement.execute();
        statement.close();
        connection.commit();
    }

    public List<Transfer> getAllTransfers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TRANSFER_TABLE);
        List<Transfer> allTransfers = new ArrayList<>();
        while (resultSet.next()) {
            Transfer transfer = new Transfer(Integer.parseInt(resultSet.getString(SENDER_ACCOUNT_COLUMN)), Integer.parseInt(resultSet.getString(RECEIVER_ACCOUNT_COLUMN)), Integer.parseInt(resultSet.getString(AMOUNT_COLUMN)));
            allTransfers.add(transfer);
        }
        statement.close();
        return allTransfers;
    }

    public void clearClients() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM " + USER_TABLE + ";");
        statement.execute();
        statement.close();
        connection.commit();
    }

    public void clearTransfers() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TRANSFER_TABLE + ";");
        statement.execute();
        statement.close();
        connection.commit();
    }
}
