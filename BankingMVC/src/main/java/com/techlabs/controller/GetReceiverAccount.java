package com.techlabs.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.techlabs.db.DatabaseConnection;

@WebServlet("/GetReceiverAccountServlet")
public class GetReceiverAccount extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String receiverUserId = request.getParameter("receiverUserId");
        String result = "Not Found";
        
        if (receiverUserId != null && !receiverUserId.trim().isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String userQuery = "SELECT id FROM users WHERE email = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(userQuery)) {
                    pstmt.setString(1, receiverUserId);
                    ResultSet rs = pstmt.executeQuery();
                    
                    if (rs.next()) {
                        int userId = rs.getInt("id");
                        
                        String accountQuery = "SELECT accountNumber FROM account WHERE userId = ?";
                        try (PreparedStatement accountStmt = conn.prepareStatement(accountQuery)) {
                            accountStmt.setInt(1, userId);
                            ResultSet accountRs = accountStmt.executeQuery();
                            
                            if (accountRs.next()) {
                                result = accountRs.getString("accountNumber");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error";
            }
        }
        
        response.setContentType("text/plain");
        response.getWriter().write(result);
    }
}