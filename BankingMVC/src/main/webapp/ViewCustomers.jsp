<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View Customers</title>
</head>
<body>
	<h1>Customer List</h1>

	<table border="1">
		<tr>
			<th>First Name</th>
			<th>Last Name</th>
			<th>Email ID</th>
			<th>Account Number</th>
			<th>Balance</th>
		</tr>
		<c:forEach var="customer" items="${customers}">
			<tr>
				<td>${customer.firstName}</td>
				<td>${customer.lastName}</td>
				<td>${customer.emailId}</td>
				<td>${customer.accountNumber}</td>
				<td>${customer.balance}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
