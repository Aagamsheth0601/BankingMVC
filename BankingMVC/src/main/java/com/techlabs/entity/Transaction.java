package com.techlabs.entity;

import java.sql.Timestamp;

public class Transaction {
	private int transactionId;
	private String senderAccount;
	private String receiverAccount;
	private String transaction_type;
	private double amount;
	private String date;
	private int userId;
	private Timestamp transactionDate;

	public Transaction() {
	}

	public Transaction(String senderAccount, String receiverAccount, String transaction_type, double amount,
			String date) {
		this.senderAccount = senderAccount;
		this.receiverAccount = receiverAccount;
		this.transaction_type = transaction_type;
		this.amount = amount;
		this.date = date;
	}

	public Transaction(int transactionId, String senderAccount, String receiverAccount, String transaction_type, double amount,
			String date, int userId) {
		this.transactionId = transactionId;
		this.senderAccount = senderAccount;
		this.receiverAccount = receiverAccount;
		this.transaction_type = transaction_type;
		this.amount = amount;
		this.date = date;
		this.userId = userId;
	}

	public Transaction(int transactionId, String senderAccount, String receiverAccount, String transactionType,
			double amount, Timestamp transactionDate) {
		this.transactionId = transactionId;
		this.senderAccount = senderAccount;
		this.receiverAccount = receiverAccount;
		this.transaction_type = transactionType;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}

	public int getTransactionId() {
	    return transactionId;
	}

	public void setTransactionId(int transactionId) {
	    this.transactionId = transactionId;
	}



	public String getSenderAccount() {
		return senderAccount;
	}

	public void setSenderAccount(String senderAccount) {
		this.senderAccount = senderAccount;
	}

	public String getReceiverAccount() {
		return receiverAccount;
	}

	public void setReceiverAccount(String receiverAccount) {
		this.receiverAccount = receiverAccount;
	}

	public String getTransactionType() {
		return transaction_type;
	}

	public void setTransactionType(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}
}