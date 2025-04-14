package com.techlabs.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.techlabs.db.DatabaseConnection;
import com.techlabs.entity.Transaction;

@WebServlet("/view-transaction")
public class ViewTransactionsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            session.setAttribute("message", "Session expired. Please log in again.");
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get the user's account number
        String accountNumber = getSenderAccount(userId);
        if (accountNumber == null) {
            session.setAttribute("message", "No linked account found. Please contact support.");
            response.sendRedirect("CustomerHome.jsp");
            return;
        }
        
        // Fetch account balance
        double accountBalance = getAccountBalance(accountNumber);
        request.setAttribute("accountNumber", accountNumber);
        request.setAttribute("accountBalance", String.format("%.2f", accountBalance));
        
        // Get filter parameter for transaction type
        String filterType = request.getParameter("filterType");
        
        // Fetch transactions
        List<Transaction> transactions = getTransactions(accountNumber, filterType);
        request.setAttribute("transactions", transactions);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("ViewTransactions.jsp");
        dispatcher.forward(request, response);
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
    
    private List<Transaction> getTransactions(String accountNumber, String filterType) {
        List<Transaction> transactions = new ArrayList<>();
        
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM transactions WHERE (senderAccount = ? OR receiverAccount = ?)");
        
        if (filterType != null && !filterType.isEmpty() && !filterType.equals("ALL")) {
            queryBuilder.append(" AND transaction_type = ?");
        }
        
        queryBuilder.append(" ORDER BY transactionDate DESC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
            
            int paramIndex = 1;
            pstmt.setString(paramIndex++, accountNumber);
            pstmt.setString(paramIndex++, accountNumber);
            
            if (filterType != null && !filterType.isEmpty() && !filterType.equals("ALL")) {
                pstmt.setString(paramIndex++, filterType);
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getInt("transactionId"),
                    rs.getString("senderAccount"),
                    rs.getString("receiverAccount"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getTimestamp("transactionDate")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return transactions;
    }
}