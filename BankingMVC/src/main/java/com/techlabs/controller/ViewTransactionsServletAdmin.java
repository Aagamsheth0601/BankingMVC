package com.techlabs.controller;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;

import com.techlabs.db.DatabaseConnection;
import com.techlabs.entity.Transaction;

import javax.servlet.annotation.WebServlet;

@WebServlet("/admin/view-transactions")
public class ViewTransactionsServletAdmin extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
        	Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM transactions");

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("transactionId"));
                t.setUserId(rs.getInt("userId"));
                t.setAmount(rs.getDouble("amount"));
                t.setSenderAccount(rs.getString("senderAccount"));
                t.setReceiverAccount(rs.getString("receiverAccount"));
                t.setTransactionType(rs.getString("transaction_type"));
                transactions.add(t);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("transactions", transactions);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/ViewTransactionsAdmin.jsp");
        dispatcher.forward(request, response);
    }
}

