<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transaction History</title>
</head>
<body>
    <div class="container mt-4">
        <h2 class="mb-4">
            <i class="fas fa-history"></i> Transaction History
        </h2>
        
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">Account Information</h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Account Number:</strong> ${accountNumber}</p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Current Balance:</strong> $${accountBalance}</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="filter-section">
            <form action="${pageContext.request.contextPath}/view-transaction" method="get">
                <div class="form-group">
                    <label for="filterType" class="mr-2">Transaction Type:</label>
                    <select name="filterType" id="filterType" class="form-control">
                        <option value="ALL" ${param.filterType == 'ALL' || param.filterType == null ? 'selected' : ''}>All Transactions</option>
                        <option value="CREDIT" ${param.filterType == 'CREDIT' ? 'selected' : ''}>CREDIT</option>
                        <option value="DEBIT" ${param.filterType == 'DEBIT' ? 'selected' : ''}>DEBIT</option>
                        <option value="BANK_TRANSFER" ${param.filterType == 'BANK_TRANSFER' ? 'selected' : ''}>TRANSFER</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-filter"></i> Filter
                </button>
            </form>
        </div>
        
        <c:if test="${empty transactions}">
            <div class="alert alert-info">
                <i class="fas fa-info-circle"></i> No transactions found for the selected criteria.
            </div>
        </c:if>
        
        <c:if test="${not empty transactions}">
            <div class="list-group">
                <c:forEach var="transaction" items="${transactions}">
                    <c:set var="transactionClass" value="transaction-card" />
                    <c:set var="amountClass" value="" />
                    <c:set var="transactionIcon" value="" />
                    <c:set var="displayAmount" value="" />
                    
                    <c:choose>
                        <c:when test="${transaction.transactionType == 'CREDIT'}">
                            <c:set var="transactionClass" value="${transactionClass} credit" />
                            <c:set var="amountClass" value="amount-credit" />
                            <c:set var="transactionIcon" value="fa-plus-circle" />
                            <c:set var="displayAmount" value="+ $${transaction.amount}" />
                            <c:set var="description" value="Credit to your account" />
                        </c:when>
                        <c:when test="${transaction.transactionType == 'DEBIT'}">
                            <c:set var="transactionClass" value="${transactionClass} debit" />
                            <c:set var="amountClass" value="amount-debit" />
                            <c:set var="transactionIcon" value="fa-minus-circle" />
                            <c:set var="displayAmount" value="- $${transaction.amount}" />
                            <c:set var="description" value="Debit from your account" />
                        </c:when>
                        <c:when test="${transaction.transactionType == 'BANK_TRANSFER' && transaction.senderAccount == accountNumber}">
                            <c:set var="transactionClass" value="${transactionClass} transfer-out" />
                            <c:set var="amountClass" value="amount-debit" />
                            <c:set var="transactionIcon" value="fa-arrow-right" />
                            <c:set var="displayAmount" value="- $${transaction.amount}" />
                            <c:set var="description" value="Transfer to account ${transaction.receiverAccount}" />
                        </c:when>
                        <c:when test="${transaction.transactionType == 'BANK_TRANSFER' && transaction.receiverAccount == accountNumber}">
                            <c:set var="transactionClass" value="${transactionClass} transfer-in" />
                            <c:set var="amountClass" value="amount-credit" />
                            <c:set var="transactionIcon" value="fa-arrow-left" />
                            <c:set var="displayAmount" value="+ $${transaction.amount}" />
                            <c:set var="description" value="Transfer from account ${transaction.senderAccount}" />
                        </c:when>
                    </c:choose>
                    
                    <div class="list-group-item ${transactionClass}">
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1">
                                <i class="fas ${transactionIcon}"></i> 
                                ${description}
                            </h5>
                            <small>
                                <fmt:formatDate value="${transaction.transactionDate}" pattern="MMM dd, yyyy HH:mm:ss" />
                            </small>
                        </div>
                        <div class="d-flex w-100 justify-content-between mt-2">
                            <p class="mb-1">Transaction ID: ${transaction.transactionId}</p>
                            <h5 class="${amountClass}">${displayAmount}</h5>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        
        <div class="mt-4">
            <a href="customer.jsp" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Back to Home
            </a>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>