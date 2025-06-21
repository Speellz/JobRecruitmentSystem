<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Company Registration Rejected</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-light d-flex flex-column justify-content-center align-items-center vh-100">

<div class="card shadow p-5 text-center bg-white rounded" style="max-width: 500px; width: 90%;">
    <h2 class="text-danger mb-3">Your company registration has been rejected.</h2>
    <p class="mb-4 fs-5">Please contact support.</p>
    <a href="<%= request.getContextPath() %>/" class="btn btn-primary btn-lg">Back</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
