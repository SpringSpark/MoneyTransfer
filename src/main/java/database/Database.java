package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import exceptions.DataValidationException;
import model.Client;
import model.Transfer;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.PersistenceException;

public class Database {

    private static final String URL = "jdbc:h2:mem:db";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";
    private static Database instance;

    private static SessionFactory sessionFactory;

    private Database() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(URL);
        ds.setUser(USER);
        ds.setPassword(PASSWORD);

        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void createNewClient(Client client) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(client);
        session.getTransaction().commit();
    }

    public Client findClientByAccount(int account) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Client client;
        try {
            client = session.load(Client.class, account);
            Hibernate.initialize(client);

        } catch (HibernateException e) {
            System.out.println("Client with account " + account + " is not found:" + e.getMessage());
            client = null;
        }
        session.getTransaction().commit();
        return client;
    }

    public List<Client> getAllClients() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List clients = session.createQuery("FROM Client").list();
        session.getTransaction().commit();
        return clients;
    }

    public void createNewTransfer(Transfer transfer) {
        if (findClientByAccount(transfer.getSenderAccount()) == null || findClientByAccount(transfer.getReceiverAccount()) == null) {
            throw new DataValidationException("Invalid account number for transfer.");
        }
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(transfer);
        session.getTransaction().commit();
    }

    public List<Transfer> getAllTransfers() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List transfers = session.createQuery("FROM Transfer").list();
        session.getTransaction().commit();
        return transfers;
    }

    public void clearClients() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM Client").executeUpdate();
        session.getTransaction().commit();
    }

    public void clearTransfers() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM Transfer").executeUpdate();
        session.getTransaction().commit();
    }
}
