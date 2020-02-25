package com.karpovich.homework.database;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class Database {

    private static final String URL = "jdbc:h2:mem:db";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";
    private static Database instance;

    private static Session session;
    private static SessionFactory sessionFactory;

    private Database() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(URL);
        ds.setUser(USER);
        ds.setPassword(PASSWORD);

        sessionFactory = HibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Session getSession() {
        return session;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
