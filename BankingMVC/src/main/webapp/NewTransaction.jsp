<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Transaction</title>
 
    <script>
        function fetchReceiverAccount() {
            var receiverUserId = document.getElementById("receiverUserId").value;
            if (receiverUserId.trim() === "") {
                document.getElementById("receiverAccount").value = "";
                document.getElementById("receiverAccountInfo").style.display = "none";
                return;
            }

            var xhr = new XMLHttpRequest();
            xhr.open("GET", "GetReceiverAccountServlet?receiverUserId=" + receiverUserId, true);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var response = xhr.responseText.trim();
                    if (response === "Not Found") {
                        document.getElementById("receiverAccount").value = "";
                        document.getElementById("receiverAccountInfo").style.display = "none";
                        alert("Receiver account not found. Please check the email ID.");
                    } else if (response === "Error") {
                        document.getElementById("receiverAccount").value = "";
                        document.getElementById("receiverAccountInfo").style.display = "none";
                        alert("Error retrieving account information. Please try again.");
                    } else {
                        document.getElementById("receiverAccount").value = response;
                        document.getElementById("receiverAccountInfo").style.display = "block";
                        document.getElementById("receiverAccountDisplay").innerHTML = response;
                    }
                }
            };
            xhr.send();
        }

        function validateForm() {
            var transactionType = document.getElementById("transactionType").value;
            var amount = document.getElementById("amount").value;
            var senderAccount = document.getElementById("senderAccount").value;
            var senderBalance = <%= session.getAttribute("senderBalance") != null ? session.getAttribute("senderBalance") : "0" %>;
            
            if (transactionType === "") {
                alert("Please select a transaction type.");
                return false;
            }
            
            if (isNaN(amount) || parseFloat(amount) <= 0) {
                alert("Please enter a valid amount greater than zero.");
                return false;
            }
            
            if ((transactionType === "DEBIT" || transactionType === "BANK_TRANSFER") && parseFloat(amount) > parseFloat(senderBalance)) {
                alert("Insufficient funds. Your current balance is $" + senderBalance);
                return false;
            }
            
            if (transactionType === "BANK_TRANSFER") {
                var receiverAccount = document.getElementById("receiverAccount").value;
                if (receiverAccount === "") {
                    alert("Please select a valid receiver account.");
                    return false;
                }
                
                if (senderAccount === receiverAccount) {
                    alert("Cannot transfer to your own account.");
                    return false;
                }
            }
            
            return true;
        }
        
        function toggleReceiverField() {
            var transactionType = document.getElementById("transactionType").value;
            var receiverFields = document.getElementById("receiverFields");
            
            if (transactionType === "BANK_TRANSFER") {
                receiverFields.style.display = "block";
            } else {
                receiverFields.style.display = "none";
                document.getElementById("receiverUserId").value = "";
                document.getElementById("receiverAccount").value = "";
                document.getElementById("receiverAccountInfo").style.display = "none";
            }
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>New Transaction</h2>
        
        <%
            HttpSession sessionObj = request.getSession();
            String message = (String) sessionObj.getAttribute("message");
            String senderAccount = (String) sessionObj.getAttribute("senderAccount");
            String senderBalance = (String) sessionObj.getAttribute("senderBalance");
            String messageClass = "message";
            
            if (message != null) {
                if (message.contains("successful")) {
                    messageClass += " success";
                }
        %>
            <div class="<%= messageClass %>"><%= message %></div>
        <%
                sessionObj.removeAttribute("message");
            }

            if (senderAccount == null) {
        %>
            <div class="message">No linked account found. Please contact support.</div>
        <%
            } else {
        %>
        
        <div class="account-info">
            <p><strong>Your Account:</strong> <%= senderAccount %></p>
            <p><strong>Available Balance:</strong> <span class="account-balance">$<%= senderBalance %></span></p>
        </div>

        <form action="${pageContext.request.contextPath}/new-transaction" method="post" onsubmit="return validateForm()">
            <label for="transactionType">Transaction Type:</label>
            <select name="transactionType" id="transactionType" required onchange="toggleReceiverField()">
                <option value="">-- Select Transaction Type --</option>
                <option value="CREDIT">Credit</option>
                <option value="DEBIT">Debit</option>
                <option value="BANK_TRANSFER">Bank Transfer</option>
            </select>

            <input type="hidden" id="senderAccount" name="senderAccount" value="<%= senderAccount %>">

            <div id="receiverFields" style="display: none;">
                <label for="receiverUserId">Receiver Email ID:</label>
                <input type="text" id="receiverUserId" name="receiverUserId" placeholder="Enter receiver's account number" onblur="fetchReceiverAccount()">

                <div id="receiverAccountInfo" style="display: none;" class="account-info">
                    <p><strong>Receiver Account:</strong> <span id="receiverAccountDisplay"></span></p>
                </div>

                <input type="hidden" id="receiverAccount" name="receiverAccount">
            </div>

            <label for="amount">Transaction Amount ($):</label>
            <input type="text" id="amount" name="amount" placeholder="Enter amount" required>

            <input type="submit" value="Submit Transaction">
        </form>

        <a href="customer.jsp" class="link">Back to Home</a>

        <script>
            toggleReceiverField();
        </script>

        <%
            }
        %>
    </div>
</body>
</html>