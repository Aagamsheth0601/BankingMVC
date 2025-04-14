<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Transaction</title>
 
    <script>
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
                var receiverUserId = document.getElementById("receiverUserId").value;
                if (receiverUserId === "") {
                    alert("Please enter receiver's email ID.");
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
                document.getElementById("receiverInfo").style.display = "none";
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

        <!-- Step 1: Initial form to select transaction type -->
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
                <input type="text" id="receiverUserId" name="receiverUserId" placeholder="Enter receiver's email ID">
                
                <input type="submit" name="action" value="Verify Receiver" formaction="${pageContext.request.contextPath}/GetReceiverAccountServlet">
                
                <% 
                String receiverAccount = (String) sessionObj.getAttribute("receiverAccount");
                String receiverStatus = (String) sessionObj.getAttribute("receiverStatus");
                
                if (receiverStatus != null && receiverStatus.equals("FOUND") && receiverAccount != null) { 
                %>
                <div id="receiverInfo" class="account-info">
                    <p><strong>Receiver Account:</strong> <%= receiverAccount %></p>
                    <input type="hidden" name="receiverAccount" value="<%= receiverAccount %>">
                </div>
                <% 
                } else if (receiverStatus != null && receiverStatus.equals("NOT_FOUND")) { 
                %>
                <div class="message">Receiver account not found. Please check the email ID.</div>
                <% 
                }
                %>
            </div>

            <label for="amount">Transaction Amount ($):</label>
            <input type="text" id="amount" name="amount" placeholder="Enter amount" required>

            <input type="submit" name="action" value="Submit Transaction">
        </form>

        <a href="customer.jsp" class="link">Back to Home</a>

        <script>
            toggleReceiverField();
            
            // If transaction type was previously selected, restore it
            <% String prevType = request.getParameter("transactionType"); 
               if (prevType != null && !prevType.isEmpty()) { %>
               document.getElementById("transactionType").value = "<%= prevType %>";
               toggleReceiverField();
            <% } %>
            
            // If receiver email was previously entered, restore it
            <% String prevReceiver = request.getParameter("receiverUserId"); 
               if (prevReceiver != null && !prevReceiver.isEmpty()) { %>
               document.getElementById("receiverUserId").value = "<%= prevReceiver %>";
            <% } %>
        </script>

        <%
            }
        %>
    </div>
</body>
</html>