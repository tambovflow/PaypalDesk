package com.paypal.desk;

public class Transaction {
    private int id;
    private String userFrom;
    private String userTo;
    private double amount;
    private String date;

    public Transaction(int id, String userFrom, String userTo, double amount, String date) {
        this.id = id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this. amount = amount;
        this.date = date;
    }

    @Override
    public String toString(){
        return "User{" +
                "id=" + id +
                ", user_from='" + userFrom + '\'' +
                ", user_to='" + userTo + '\'' +
                ", transaction_amount=" + amount +
                ", transaction_date=" + date +
                '}';
    }
}
