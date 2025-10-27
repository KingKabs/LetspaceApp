package com.sp.letspace.models;

public class Transaction {
    private int id;
    private int tenantId;
    private String category;     // "invoice" or "payment"
    private double amount;
    private double balance_after;
    private String date;
    private String comments;
    private String extra;    // e.g. month for invoices, receipt_no for payments

    public Transaction(int id, int tenantId, String type, double amount, String date, String comments, String extra) {
        this.id = id;
        this.tenantId = tenantId;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.comments = comments;
        this.extra = extra;
    }

    public int getId() {
        return id;
    }

    public int getTenantId() {
        return tenantId;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balance_after;
    }

    public String getDate() {
        return date;
    }

    public String getComments() {
        return comments;
    }

    public String getExtra() {
        return extra;
    }
}

