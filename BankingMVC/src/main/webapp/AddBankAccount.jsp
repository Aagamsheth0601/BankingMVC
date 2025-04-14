<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.techlabs.db.DatabaseConnection"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Bank Account</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="container mt-5">
	<%
	HttpSession sess = request.getSession(false);
	if (sess == null) {
		response.sendRedirect(request.getContextPath() + "/login");
		return;
	}

	Object userIdObj = sess.getAttribute("userId");
	String userId = null;
	if (userIdObj != null) {
		userId = userIdObj.toString();
		System.out.println("User ID in session: " + userId);
	} else {
		System.out.println("userId is missing in session.");
	}

	Boolean hasAccount = (Boolean) sess.getAttribute("hasAccount");
	String existingAccountNumber = (String) sess.getAttribute("existingAccountNumber");
	%>

	<h2 class="text-center">Add Bank Account</h2>

	<%
	String message = (String) sess.getAttribute("message");
	if (message != null) {
	%>
	<div class="alert alert-info alert-dismissible fade show" role="alert">
		<%=message%>
		<button type="button" class="btn-close" data-bs-dismiss="alert"
			aria-label="Close"></button>
	</div>
	<%
	sess.removeAttribute("message");
	}
	%>

	<div class="card p-4 shadow">
		<form action="${pageContext.request.contextPath}/admin/add-account"
			method="POST">
			<div class="mb-3">
				<label class="form-label">Enter Customer ID:</label> <input
					type="text" name="userId" class="form-control" required>
			</div>
			<button type="submit" name="action" value="search"
				class="btn btn-primary">Search</button>
		</form>
	</div>

	<br>

	<%
	String firstName = (String) sess.getAttribute("firstName");
	String lastName = (String) sess.getAttribute("lastName");
	String email = (String) sess.getAttribute("email");

	if (firstName != null && lastName != null && email != null) {
	%>
	<div class="card mt-4 p-4 shadow">
		<h3>Customer Details</h3>
		<p>
			<strong>Name:</strong>
			<%=firstName%>
			<%=lastName%></p>
		<p>
			<strong>Email:</strong>
			<%=email%></p>

		<%
		if (hasAccount != null && hasAccount) {
		%>
		<div class="alert alert-warning">
			<strong>Note:</strong> This customer already has an account with
			number:
			<%=existingAccountNumber%>
			<br>Only one account per customer is allowed.
		</div>
		<%
		} else {
		%>
		<form method="POST"
			action="${pageContext.request.contextPath}/admin/add-account">
			<input type="hidden" name="userId" value="<%=userId%>">
			<button type="submit" name="action" value="generate"
				class="btn btn-success">Generate Account Number</button>
		</form>
		<%
		}
		%>
	</div>
	<%
	}
	sess.removeAttribute("firstName");
	sess.removeAttribute("lastName");
	sess.removeAttribute("email");
	%>

	<%
	String accountNumber = (String) sess.getAttribute("generatedAccountNumber");
	if (accountNumber != null && userId != null && (hasAccount == null || !hasAccount)) {
	%>
	<div class="card mt-4 p-4 shadow">
		<p>
			<strong>Generated Account Number:</strong>
			<%=accountNumber%></p>

		<!-- Add Bank Account Form -->
		<form action="${pageContext.request.contextPath}/admin/add-account"
			method="POST">
			<input type="hidden" name="userId" value="<%=userId%>"> <input
				type="hidden" name="accountNumber" value="<%=accountNumber%>">
			<div class="mb-3">
				<label class="form-label">Account Number:</label> <input type="text"
					value="<%=accountNumber%>" class="form-control" disabled>
			</div>
			<button type="submit" name="action" value="add"
				class="btn btn-primary">Create Account</button>
		</form>
	</div>
	<%
	}
	%>

	<div class="mt-4">
		<a href="${pageContext.request.contextPath}/admin"
			class="btn btn-secondary">Back to Admin Dashboard</a>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>