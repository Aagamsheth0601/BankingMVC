package com.techlabs.controller;

import com.techlabs.db.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/add-customer")
public class AddCustomerServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseConnection dbConnection;

	@Override
	public void init() throws ServletException {
		dbConnection = new DatabaseConnection();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/AddCustomer.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dbConnection.getConnection();

			String sql = "INSERT INTO users (firstName, lastName, emailId, password) VALUES (?, ?, ?, ?)";

			preparedStatement  = connection.prepareStatement(sql);
			preparedStatement .setString(1, firstName);
			preparedStatement .setString(2, lastName);
			preparedStatement .setString(3, email);
			preparedStatement .setString(4, password);

			int rowsAffected = preparedStatement .executeUpdate();
		
			HttpSession session = request.getSession();
			
			if (rowsAffected > 0) {
				session.setAttribute("successMessage", "Customer added successfully!");

				response.sendRedirect(request.getContextPath() + "/admin");
			} else {
				session.setAttribute("errorMessage", "Failed to add customer.");

				response.sendRedirect(request.getContextPath() + "/AddCustomer.jsp");
			}
		} catch (SQLException e) {
			HttpSession session = request.getSession();
			session.setAttribute("errorMessage", "Error adding customer: " + e.getMessage());

			response.sendRedirect(request.getContextPath() + "/AddCustomer.jsp");
		} finally {
			try {
				if (preparedStatement  != null)
					preparedStatement .close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}