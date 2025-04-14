package com.techlabs.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.techlabs.db.DatabaseConnection;

@WebServlet("/new-transaction")
public class NewTransactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId"); 

        if (userId == null) {
            session.setAttribute("message", "Session expired. Please log in again.");
            response.sendRedirect("login.jsp");
            return;
        }

        String senderAccount = getSenderAccount(userId);
        double senderBalance = getAccountBalance(senderAccount);

        if (senderAccount == null) {
            session.setAttribute("message", "No linked account found. Please contact support.");
        } else {
            session.setAttribute("senderAccount", senderAccount);
            session.setAttribute("senderBalance", String.format("%.2f", senderBalance));
        }

        response.sendRedirect("NewTransaction.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            session.setAttribute("message", "Session expired. Please log in again.");
            response.sendRedirect("login.jsp");
            return;
        }

        String transactionType = request.getParameter("transactionType");
        String senderAccount = request.getParameter("senderAccount");
        String receiverAccount = request.getParameter("receiverAccount");
        double amount = 0;
        
        try {
            amount = Double.parseDouble(request.getParameter("amount"));
        } catch (NumberFormatException e) {
            session.setAttribute("message", "Invalid amount entered.");
            response.sendRedirect("NewTransaction.jsp");
            return;
        }

        if (senderAccount == null || senderAccount.isEmpty()) {
            session.setAttribute("message", "No linked account found. Please contact support.");
            response.sendRedirect("NewTransaction.jsp");
            return;
        }
        
        // For DEBIT and CREDIT, set receiverAccount to senderAccount
        if ("DEBIT".equals(transactionType) || "CREDIT".equals(transactionType)) {
            receiverAccount = senderAccount;
        }

        System.out.println("Processing transaction type: " + transactionType);
        System.out.println("Sender: " + senderAccount);
        System.out.println("Receiver: " + receiverAccount);
        System.out.println("Amount: " + amount);

        boolean success = processTransaction(userId, senderAccount, receiverAccount, transactionType, amount);

        if (success) {
            double newBalance = getAccountBalance(senderAccount);
            session.setAttribute("senderBalance", String.format("%.2f", newBalance));
            session.setAttribute("message", "Transaction successful!");
        } else {
            session.setAttribute("message", "Transaction failed. Please try again.");
        }

        response.sendRedirect("NewTransaction.jsp");
    }

    private String getSenderAccount(int userId) {
        String query = "SELECT accountNumber FROM account WHERE userId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("accountNumber");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private double getAccountBalance(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return 0;
        }
        
        String query = "SELECT balance FROM account WHERE accountNumber = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean processTransaction(int userId, String senderAccount, String receiverAccount, String transactionType, double amount) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Step 1: Insert transaction record
            String insertQuery = "INSERT INTO transactions (userId, senderAccount, receiverAccount, transaction_type, amount) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setInt(1, userId);  
                pstmt.setString(2, senderAccount);
                pstmt.setString(3, receiverAccount);
                pstmt.setString(4, transactionType);
                pstmt.setDouble(5, amount);
                pstmt.executeUpdate();
            }

            switch (transactionType) {
                case "CREDIT":
                    // Add amount to sender's account
                    updateBalance(conn, senderAccount, amount, true);
                    break;
                    
                case "DEBIT":
                    // Check if sender has sufficient balance
                    updateBalance(conn, senderAccount, amount, false);
                    break;
                    
                case "BANK_TRANSFER":
                	  double receiverBalance = getAccountBalanceWithConnection(conn, receiverAccount);
                      if (receiverBalance < 0) { // Account doesn't exist
                          conn.rollback();
                          return false;
                      }
                      
                      // Subtract from sender
                      updateBalance(conn, senderAccount, amount, false);
                      // Add to receiver
                      updateBalance(conn, receiverAccount, amount, true);
                      break;
                      
                  default:
                      conn.rollback();
                      return false;
              }
            conn.commit();
            return true;
            
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    
    private double getAccountBalanceWithConnection(Connection conn, String accountNumber) throws SQLException {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return -1;
        }
        
        String query = "SELECT balance FROM account WHERE accountNumber = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        }
        return -1;
    }
    private void updateBalance(Connection conn, String accountNumber, double amount, boolean isCredit) throws SQLException {
        String query;
        if (isCredit) {
            query = "UPDATE account SET balance = balance + ? WHERE accountNumber = ?";
        } else {
            query = "UPDATE account SET balance = balance - ? WHERE accountNumber = ?";
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNumber);
            pstmt.executeUpdate();
        }
    }
}