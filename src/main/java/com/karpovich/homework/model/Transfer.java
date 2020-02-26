package com.karpovich.homework.model;

import com.karpovich.homework.exceptions.DataValidationException;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "transfer")
@Entity
@NamedQueries({
        @NamedQuery(name = "model.Transfer.findAll",
                query = "select t from Transfer t"),
})
public class Transfer {
    @Id
    @GeneratedValue
    private long id;
    private long senderAccount;
    private long receiverAccount;
    private long amount;

    public Transfer(long senderAccount, long receiverAccount, long amount) throws DataValidationException {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
    }

    public Transfer() {
    }

    public long getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(long senderAccount) {
        this.senderAccount = senderAccount;
    }

    public long getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(long receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", senderAccount=" + senderAccount +
                ", receiverAccount=" + receiverAccount +
                ", amount=" + amount +
                '}';
    }
}
