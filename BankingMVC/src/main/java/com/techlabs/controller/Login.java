package com.techlabs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.techlabs.entity.User;

@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserController userController;

	public Login() {
		userController = new UserController();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String emailId = request.getParameter("emailId");
	    String password = request.getParameter("password");

	    User user = userController.getUserByIdAndPassword(emailId, password);

	    if (user != null) {
	        HttpSession session = request.getSession(true);
	        session.setAttribute("user", user);
	        session.setAttribute("userId", user.getUserId());  

	        System.out.println("User logged in: " + user.getUserId());  

	        if ("ADMIN".equals(user.getUserType())) {
	            response.sendRedirect(request.getContextPath() + "/admin");
	        } else if ("CUSTOMER".equals(user.getUserType())) {
	            response.sendRedirect(request.getContextPath() + "/customer");
	        }
	    } else {
	        System.out.println("Invalid login attempt for email: " + emailId); 
	        request.getSession().setAttribute("errorMessage", "Invalid Email or Password");
	        response.sendRedirect(request.getContextPath() + "/login");
	    }
	}

	}

