package be.ecam.basics.exercises;

import java.util.Objects;

public class Account {
    private static final double EPS = 1e-9;

    private double balance;

    public Account() { this(0.00); }
    public Account(double initial) { this.balance = round2(initial); }

    public double getBalance() { return round2(balance); }

    public void deposit(double amount) {
        if (amount < 0) throw new IllegalArgumentException("amount");
        balance = round2(balance + round2(amount));
    }

    public void withdraw(double amount) {
        if (amount < 0) throw new IllegalArgumentException("amount");
        amount = round2(amount);
        if (amount - balance > EPS) throw new IllegalStateException("insufficient");
        balance = round2(balance - amount);
    }

    public void transferTo(Account other, double amount) {
        Objects.requireNonNull(other, "other");
        withdraw(amount);
        other.deposit(amount);
    }

    private static double round2(double x) {
        return Math.round(x * 100.0d) / 100.0d;
    }
}
