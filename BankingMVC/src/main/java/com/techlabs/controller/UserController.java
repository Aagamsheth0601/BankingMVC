package com.techlabs.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.techlabs.db.DatabaseConnection;
import com.techlabs.entity.User;

public class UserController {

	public User getUserByIdAndPassword(String emailId, String password) {
	    String query = "SELECT userId, emailId, firstName, lastName, password, userType FROM users WHERE emailId = ? AND password = ?";
	    User user = null;

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = conn.prepareStatement(query)) {

	        preparedStatement.setString(1, emailId);
	        preparedStatement.setString(2, password);

	        try (ResultSet rs = preparedStatement.executeQuery()) {
	            if (rs.next()) {
	                user = new User();
	                user.setUserId(rs.getInt("userId")); 
	                user.setEmailId(rs.getString("emailId"));
	                user.setFirstName(rs.getString("firstName"));
	                user.setLastName(rs.getString("lastName"));
	                user.setPassword(rs.getString("password"));
	                user.setUserType(rs.getString("userType"));

	                System.out.println("Fetched User ID: " + user.getUserId()); 
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return user;
	}
}
