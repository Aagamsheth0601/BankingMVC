<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.techlabs.entity.Transaction" %>
<!DOCTYPE html>
<html>
<head>
    <title>All Transactions</title>
</head>
<body>

    <h2>All Transactions</h2>

    <table border="1" cellpadding="8">
        <tr>
            <th>Transaction ID</th>
            <th>User ID</th>
            <th>Sender Account</th>
            <th>Receiver Account</th>
            <th>Type</th>
            <th>Amount</th>
         
        </tr>

        <%
            List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
            if (transactions != null && !transactions.isEmpty()) {
                for (Transaction t : transactions) {
        %>
            <tr>
                <td><%= t.getTransactionId() %></td>
                <td><%= t.getUserId() %></td>
                <td><%= t.getSenderAccount() %></td>
                <td><%= t.getReceiverAccount() %></td>
                <td><%= t.getTransactionType() %></td>
                <td><%= t.getAmount() %></td>
               
            </tr>
        <%
                }
            } else {
        %>
            <tr><td colspan="8">No transactions found.</td></tr>
        <%
            }
        %>
    </table>

</body>
</html>
