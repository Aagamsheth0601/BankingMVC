package com.techlabs.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.techlabs.db.DatabaseConnection;

import java.io.*;
import java.sql.*;

@WebServlet("/edit-profile")
public class EditProfileServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseConnection dbConnection;

	@Override
	public void init() throws ServletException {
		dbConnection = new DatabaseConnection();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
		Connection conn = null;

        int userId = (int) session.getAttribute("userId");

        try {
        	conn = dbConnection.getConnection();

            String query = "SELECT firstName, lastName, emailId, password FROM users WHERE userId=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                request.setAttribute("firstName", rs.getString("firstName"));
                request.setAttribute("lastName", rs.getString("lastName"));
                request.setAttribute("emailId", rs.getString("emailId"));
                request.setAttribute("password", rs.getString("password"));
            }

            conn.close();
            RequestDispatcher rd = request.getRequestDispatcher("editProfile.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            response.getWriter().println("DB Error: " + e.getMessage());
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String emailId = request.getParameter("emailId");
        String password = request.getParameter("password");
		Connection conn = null;

        try {
        	conn = dbConnection.getConnection();
            String update = "UPDATE users SET firstName=?, lastName=?, emailId=?, password=? WHERE userId=?";
            PreparedStatement ps = conn.prepareStatement(update);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, emailId);
            ps.setString(4, password);
            ps.setInt(5, userId);

            int updated = ps.executeUpdate();
            conn.close();

            if (updated > 0) {
                response.sendRedirect("customer.jsp");
            } else {
                response.getWriter().println("Profile update failed.");
            }

        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
