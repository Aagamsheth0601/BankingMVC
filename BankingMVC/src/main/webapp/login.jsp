		<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
		<!DOCTYPE html>
		<html>
		<head>
		    <meta charset="UTF-8">
		    <title>Banking MVC - Login</title>
		    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
		</head>
		<body class="container mt-5 width-100">
		
		    <h1 class="mb-8 text-center">WELCOME TO SWABHAV BANK</h1>
		    
		    <% String errorMessage = (String) session.getAttribute("errorMessage");
		   if (errorMessage != null) { %>
		   <div class="alert alert-danger text-center">
		       <%= errorMessage %>
		   </div>
		   <% session.removeAttribute("errorMessage"); %>
		<% } %>
		
		    <div class="d-flex justify-content-center">
		        <form action="${pageContext.request.contextPath}/login" method="POST" class="border p-4 rounded shadow-lg">
		            <fieldset>
		                <legend class="text-center">Login</legend>
		
		                <div class="mb-3">
		                    <label for="userType" class="form-label">Login As:</label>
		                    <select id="userType" name="userType" class="form-select" required>
		                        <option value="">Select User Type</option>
		                        <option value="admin">Admin</option>
		                        <option value="customer">Customer</option>
		                    </select>
		                </div>
		
		                <div class="mb-3">
		                    <label for="emailId" class="form-label">Email ID:</label>
		                    <input type="email" id="emailId" name="emailId" class="form-control" required>
		                </div>
		
		                <div class="mb-3">
		                    <label for="password" class="form-label">Password:</label>
		                    <input type="password" id="password" name="password" class="form-control" required>
		                </div>
		
		                <div class="text-center">
		                    <button type="submit" class="btn btn-primary">Login</button>
		                </div>
		
		            </fieldset>
		        </form>
		    </div>
		
		    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
		
		</body>
		</html>
