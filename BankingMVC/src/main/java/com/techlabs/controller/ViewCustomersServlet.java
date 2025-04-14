package com.techlabs.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import com.techlabs.db.DatabaseConnection;
import com.techlabs.entity.User;

@WebServlet("/admin/view-customers")
public class ViewCustomersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DatabaseConnection dbConnection;

	@Override
	public void init() throws ServletException {
		dbConnection = new DatabaseConnection();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<User> customers = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = dbConnection.getConnection();
			preparedStatement = connection
					.prepareStatement("SELECT u.firstName, u.lastName, u.emailId, a.accountNumber, a.balance "
							+ "FROM users u " + "LEFT JOIN account a ON u.userId = a.userId " + "WHERE u.userType = ?");
			preparedStatement.setString(1, "CUSTOMER");
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				User customer = new User(rs.getString("firstName"), rs.getString("lastName"), rs.getString("emailId"),
						rs.getString("accountNumber"), rs.getDouble("balance"));
				customers.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Database Error: " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("customers", customers);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/ViewCustomers.jsp");
		dispatcher.forward(request, response);
	}
}
