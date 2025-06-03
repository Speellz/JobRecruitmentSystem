<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Applicant Sign Up</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<div class="auth-container">
    <h2>Applicant Sign Up</h2>

    <form action="<%= request.getContextPath() %>/auth/signup" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="text" name="name" placeholder="Name" required/>
        <input type="email" name="email" placeholder="Email" required/>
        <input type="password" name="password" placeholder="Password" required/>
        <button type="submit">Register</button>
    </form>

    <div style="margin-top: 20px;">
        <a href="<%= request.getContextPath() %>/login">Already have an account? Login</a>
    </div>
</div>

</body>
</html>
