package model;

import exceptions.DataValidationException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Client {
    private String name;
    private String surname;
    @Id
    private int account;

    public Client(int account, String name, String surname) {
        if (account < 0 || name == null || name.isEmpty() || surname == null || surname.isEmpty()) {
            throw new DataValidationException("Invalid client data.");
        }
        this.name = name;
        this.surname = surname;
        this.account = account;
    }

    public Client(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", account=" + account +
                '}';
    }
}
