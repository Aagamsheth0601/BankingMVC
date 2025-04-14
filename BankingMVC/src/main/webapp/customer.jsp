<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer Home - Banking App</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .card {
            margin-bottom: 20px;
            transition: transform 0.3s;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }
        .card-img-top {
            height: 150px;
            object-fit: cover;
        }
    </style>
</head>
<body>
   

    <div class="container mt-4">
        <div class="jumbotron">
            <h1>Welcome!</h1>
           <form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit" class="btn btn-danger float-right" >Logout</button>
    </form>
        </div>

        <div class="row">
            <div class="col-md-4">
                <div class="card">
                    <div class="card-img-top bg-info d-flex align-items-center justify-content-center">
                        <i class="fas fa-history fa-5x text-white"></i>
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">View Transactions</h5>
                        <p class="card-text">Check your transaction history and account activity.</p>
                        <a href="${pageContext.request.contextPath}/view-transaction" class="btn btn-primary">View Transactions</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <div class="card-img-top bg-success d-flex align-items-center justify-content-center">
                        <i class="fas fa-exchange-alt fa-5x text-white"></i>
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">New Transaction</h5>
                        <p class="card-text">Transfer money, deposit, or withdraw from your account.</p>
                        <a href="${pageContext.request.contextPath}/new-transaction" class="btn btn-primary">New Transaction</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <div class="card-img-top bg-warning d-flex align-items-center justify-content-center">
                        <i class="fas fa-user-edit fa-5x text-white"></i>
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">Edit Profile</h5>
                        <p class="card-text">Update your personal information and password.</p>
                        <a href="${pageContext.request.contextPath}/edit-profile" class="btn btn-primary">Edit Profile</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://kit.fontawesome.com/a076d05399.js"></script>
</body>
</html>