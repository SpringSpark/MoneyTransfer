package model;

import exceptions.DataValidationException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Entity
public class Transfer {
    @Id
    @GeneratedValue
    private int id;
    private int senderAccount;
    private int receiverAccount;
    private int amount;

    public Transfer(int senderAccount, int receiverAccount, int amount) {
        if (senderAccount < 0 || receiverAccount < 0 || amount < 0) {
            throw new DataValidationException("Invalid transfer data");
        }
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
    }

    public Transfer() {}

    public int getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(int senderAccount) {
        this.senderAccount = senderAccount;
    }

    public int getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(int receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
