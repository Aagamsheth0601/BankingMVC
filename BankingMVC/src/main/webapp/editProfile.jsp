<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Profile</title>
</head>
<body>
    <h2>Edit Your Profile</h2>

    <form action="${pageContext.request.contextPath}/edit-profile" method="post">
        <label>First Name:</label>
        <input type="text" name="firstName" value="${firstName}" required><br><br>

        <label>Last Name:</label>
        <input type="text" name="lastName" value="${lastName}" required><br><br>

        <label>Email ID:</label>
        <input type="email" name="emailId" value="${emailId}" required><br><br>

        <label>Password:</label>
        <input type="password" name="password" value="${password}" required><br><br>

        <input type="submit" value="Update Profile">
    </form>

</body>
</html>
