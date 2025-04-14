package com.techlabs.controller;

import com.techlabs.db.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/add-account")
public class AddBankAccountServlet extends HttpServlet {
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
		request.getRequestDispatcher("/AddBankAccount.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		if ("search".equals(action)) {
			searchCustomer(request, response, session);
		} else if ("generate".equals(action)) {
			generateAccountNumber(request, response, session);
		} else if ("add".equals(action)) {
			addBankAccount(request, response, session);
		}
	}

	private void searchCustomer(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		String userId = request.getParameter("userId");
		System.out.println("Searching customer with UserId: " + userId);

		if (userId == null || userId.trim().isEmpty()) {
			session.setAttribute("message", "Please enter a valid Customer ID.");
			response.sendRedirect(request.getContextPath() + "/AddBankAccount.jsp");
			return;
		}

		try (Connection connection = dbConnection.getConnection()) {
			String sql = "SELECT firstName, lastName, emailId FROM users WHERE userId = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setInt(1, Integer.parseInt(userId));
				try (ResultSet rs = preparedStatement.executeQuery()) {
					if (rs.next()) {
						session.setAttribute("firstName", rs.getString("firstName"));
						session.setAttribute("lastName", rs.getString("lastName"));
						session.setAttribute("email", rs.getString("emailId"));
						session.setAttribute("userId", userId);
						
						boolean hasAccount = checkIfUserHasAccount(Integer.parseInt(userId));
						session.setAttribute("hasAccount", hasAccount);
						
						if (hasAccount) {
							String existingAccountNumber = getExistingAccountNumber(Integer.parseInt(userId));
							session.setAttribute("existingAccountNumber", existingAccountNumber);
							System.out.println("User already has account: " + existingAccountNumber);
						}
					} else {
						session.setAttribute("message", "Customer not found.");
					}
				}
			}
		} catch (SQLException e) {
			session.setAttribute("message", "Error fetching customer: " + e.getMessage());
			e.printStackTrace();
		}

		response.sendRedirect(request.getContextPath() + "/AddBankAccount.jsp");
	}
	
	private boolean checkIfUserHasAccount(int userId) {
		try (Connection connection = dbConnection.getConnection()) {
			String sql = "SELECT COUNT(*) as count FROM account WHERE userId = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setInt(1, userId);
				try (ResultSet rs = preparedStatement.executeQuery()) {
					if (rs.next()) {
						return rs.getInt("count") > 0;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private String getExistingAccountNumber(int userId) {
		try (Connection connection = dbConnection.getConnection()) {
			String sql = "SELECT accountNumber FROM account WHERE userId = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setInt(1, userId);
				try (ResultSet rs = preparedStatement.executeQuery()) {
					if (rs.next()) {
						return rs.getString("accountNumber");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void generateAccountNumber(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		String userId = (String) session.getAttribute("userId");

		if (userId == null) {
			session.setAttribute("message", "No user selected. Please search for a customer first.");
			response.sendRedirect(request.getContextPath() + "/AddBankAccount.jsp");
			return;
		}
		
		boolean hasAccount = checkIfUserHasAccount(Integer.parseInt(userId));
		if (hasAccount) {
			String existingAccountNumber = getExistingAccountNumber(Integer.parseInt(userId));
			session.setAttribute("message", "This customer already has an account with number: " + existingAccountNumber);
			response.sendRedirect(request.getContextPath() + "/AddBankAccount.jsp");
			return;
		}

		Random rand = new Random();
		long accountNumber = 1000000000L + rand.nextInt(900000000); // Generate a 10-digit account number
		session.setAttribute("generatedAccountNumber", String.valueOf(accountNumber));

		System.out.println("Generated Account Number: " + accountNumber + " for UserId: " + userId);

		response.sendRedirect(request.getContextPath() + "/AddBankAccount.jsp");
	}

	private void addBankAccount(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		String userId = request.getParameter("userId");
		if (userId == null || userId.isEmpty()) {
			userId = (String) session.getAttribute("userId");
		}
		
		String accountNumber = (String) session.getAttribute("generatedAccountNumber");
		
		System.out.println("Adding account for UserId: " + userId + " with AccountNumber: " + accountNumber);
		
		if (userId == null || userId.isEmpty() || accountNumber == null || accountNumber.isEmpty()) {
			session.setAttribute("message", "Missing required information. Please try again.");
			response.sendRedirect(request.getContextPath() + "/AddBankAccount.jsp");
			return;
		}
		
		boolean hasAccount = checkIfUserHasAccount(Integer.parseInt(userId));
		if (hasAccount) {
			String existingAccountNumber = getExistingAccountNumber(Integer.parseInt(userId));
			session.setAttribute("message", "This customer already has an account with number: " + existingAccountNumber);
			response.sendRedirect(request.getContextPath() + "/AddBankAccount.jsp");
			return;
		}

		try {
			Connection connection = dbConnection.getConnection();
			if (connection == null || connection.isClosed()) {
				throw new SQLException("Database connection is closed before executing query.");
			}

			String sql = "INSERT INTO account (accountNumber, balance, userId) VALUES (?, 0, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, accountNumber);
				preparedStatement.setInt(2, Integer.parseInt(userId));

				int rowsAffected = preparedStatement.executeUpdate();
				System.out.println("Rows affected in DB: " + rowsAffected);

				if (rowsAffected > 0) {
					session.setAttribute("message", "Bank account created successfully!");
					session.removeAttribute("generatedAccountNumber");
					session.removeAttribute("hasAccount");
					session.removeAttribute("existingAccountNumber");

					response.sendRedirect(request.getContextPath() + "/admin.jsp");
					return;
				} else {
					session.setAttribute("message", "Failed to create bank account.");
				}
			}
		} catch (SQLException e) {
			session.setAttribute("message", "Error adding bank account: " + e.getMessage());
			e.printStackTrace();
		}
		
		response.sendRedirect(request.getContextPath() + "/AddBankAccount.jsp");
	}
}