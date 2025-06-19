<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Applicant Sign Up</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="navbar.jsp"/>

<div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <jsp:include page="messages.jsp"/>
    <div class="card p-4 shadow-sm" style="min-width: 350px; max-width: 400px; width: 100%;">
        <h3 class="text-center mb-4">Applicant Sign Up</h3>

        <form action="${pageContext.request.contextPath}/auth/signup" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <div class="mb-3">
                <input type="text" name="name" class="form-control" placeholder="Name" required />
            </div>

            <div class="mb-3">
                <input type="email" name="email" class="form-control" placeholder="Email" required />
            </div>

            <div class="mb-3">
                <input type="password" name="password" class="form-control" placeholder="Password" required />
            </div>

            <div class="d-grid">
                <button type="submit" class="btn btn-primary">Register</button>
            </div>
        </form>

        <div class="text-center mt-3">
            <a href="${pageContext.request.contextPath}/login">Already have an account? Login</a>
        </div>
    </div>
</div>

</body>
</html>
