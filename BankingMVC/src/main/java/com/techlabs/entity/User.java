package com.techlabs.entity;

public class User {

    private int userId; 
    private String emailId;
    private String firstName;
    private String lastName;
    private String password;
    private String userType; // "CUSTOMER" or "ADMIN"
    private String accountNumber;
    private double balance;

    public User() {}

    public User(String firstName, String lastName, String emailId) {
        this.emailId = emailId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName, String emailId, String accountNumber, double balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public User(String emailId, String firstName, String lastName, String password, String userType) {
        this.emailId = emailId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) { // ✅ Add this method
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
