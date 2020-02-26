package com.karpovich.homework.model;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;


@Entity
@NamedQueries({
        @NamedQuery(name = "model.Account.findAll",
                query = "select a from Account a"),
})
public class Account {
    @Id
    private long id;
    private long amount;

    public int getVersion() {
        return version;
    }

    @Column(name = "VERSION")
    @Version
    private int version;

    public Account(long id, long amount) {
        this.id = id;
        this.amount = amount;
    }

    public Account() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                ", version=" + version +
                '}';
    }
}
