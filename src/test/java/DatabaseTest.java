import database.Database;
import exceptions.DataValidationException;
import model.Client;
import model.Transfer;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

public class DatabaseTest {

    public static Database database;


    @BeforeAll
    public static void databaseInit() {
        database = Database.getInstance();
    }

    @AfterEach
    public void clearTables() throws SQLException {
        database.clearClients();
        database.clearTransfers();
        List<Client> clients = database.getAllClients();
        for(Client client : clients) {
            System.out.println("CLIENT_LIST " + client);
        }
    }

    @Test
    public void insertClientTest() throws SQLException {
        int account1 = 128;
        String name1 = "Vasya";
        String surname1 = "Pupkin";
        Client newClient = new Client(128, "Vasya", "Pupkin");
        database.createNewClient(newClient);
        List<Client> allClients = database.getAllClients();
        Assertions.assertEquals(1, allClients.size());
        Assertions.assertEquals(account1, allClients.get(0).getAccount());
        Assertions.assertEquals(name1, allClients.get(0).getName());
        Assertions.assertEquals(surname1, allClients.get(0).getSurname());
    }

    @Test
    public void insertTwoClientsTest() throws SQLException {
        Client newClient = new Client(128, "Vasya", "Pupkin");
        database.createNewClient(newClient);
        newClient = new Client(666, "Anya", "Lyalkina");
        database.createNewClient(newClient);
        List<Client> allClients = database.getAllClients();
        Assertions.assertEquals(2, allClients.size());
    }

    @Test
    public void insertTransferTest() throws SQLException {
        Client newClient = new Client(128, "Vasya", "Pupkin");
        database.createNewClient(newClient);
        newClient = new Client(666, "Anya", "Lyalkina");
        database.createNewClient(newClient);

        List<Client> clients = database.getAllClients();
        for(Client client : clients) {
            System.out.println("CLIENT_LIST " + client);
        }

        int senderAccount = 128;
        int receiverAccount = 666;
        int amount = 100;

        Transfer transfer = new Transfer(senderAccount, receiverAccount, amount);
        database.createNewTransfer(transfer);

        List<Transfer> allTransfers = database.getAllTransfers();
        Assertions.assertEquals(1, allTransfers.size());
        Assertions.assertEquals(senderAccount, allTransfers.get(0).getSenderAccount());
        Assertions.assertEquals(receiverAccount, allTransfers.get(0).getReceiverAccount());
        Assertions.assertEquals(amount, allTransfers.get(0).getAmount());
    }

    @Test
    public void insertInvalidAccountTransferTest() throws SQLException {
        Client newClient = new Client(666, "Anya", "Lyalkina");
        database.createNewClient(newClient);

        int senderAccount = 128;
        int receiverAccount = 666;
        int amount = 100;

        Transfer transfer = new Transfer(senderAccount, receiverAccount, amount);
        Assertions.assertThrows(DataValidationException.class, () -> {
            database.createNewTransfer(transfer);
        });
    }
}
