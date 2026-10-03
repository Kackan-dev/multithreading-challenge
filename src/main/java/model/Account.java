package model;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    String id;
    BigDecimal balance;

    private ReentrantLock reentrantLock = new ReentrantLock();

    public Account(String id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public ReentrantLock getReentrantLock() {
        return reentrantLock;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
