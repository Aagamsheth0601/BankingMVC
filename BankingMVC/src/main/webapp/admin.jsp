<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
<!-- Bootstrap CDN -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body class="bg-light">
	<div class="container mt-5">
		<div class="card shadow-sm p-4">
			<div class="card-body">
				<h1 class="card-title text-center">Admin Dashboard</h1>
				  <form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit" class="btn btn-danger float-right">Logout</button>
    </form>
				<p class="text-center">Welcome, ${user.firstName}
					${user.lastName}!</p>
				<p class="text-center">What would you like to do today?</p>
			</div>
		</div>

		<div class="row mt-4">
			<div class="col-md-6 col-lg-3 mb-3">
				<div class="card text-center" style="height: 120px">
					<div class="card-body">
						<h5 class="card-title">Add Customer</h5>
						<a href="${pageContext.request.contextPath}/admin/add-customer"
							class="btn btn-primary">Go</a>
					</div>
				</div>
			</div>

			<div class="col-md-6 col-lg-3 mb-3">
				<div class="card text-center" style="height: 120px" >
					<div class="card-body">
						<h5 class="card-title">Add Bank Account</h5>
						<a href="${pageContext.request.contextPath}/admin/add-account"
							class="btn btn-primary">Go</a>
					</div>
				</div>
			</div>

			<div class="col-md-6 col-lg-3 mb-3">
				<div class="card h-100 text-center">
					<div class="card-body">
						<h5 class="card-title">View Customers</h5>
						<a href="${pageContext.request.contextPath}/admin/view-customers"
							class="btn btn-primary">Go</a>
					</div>
				</div>
			</div>

			<div class="col-md-6 col-lg-3 mb-3">
				<div class="card h-100 text-center">
					<div class="card-body">
						<h5 class="card-title">View Transactions</h5>
						<a
							href="${pageContext.request.contextPath}/admin/view-transactions"
							class="btn btn-primary">Go</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
