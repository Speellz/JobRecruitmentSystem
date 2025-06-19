<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Users</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp" />

<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <h2 class="mb-4">User Management</h2>

    <!-- Add User Form -->
    <form action="/admin/users/create" method="post" class="row g-3 mb-4">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <div class="col-md-3">
            <input type="text" name="name" class="form-control" placeholder="Name" required>
        </div>
        <div class="col-md-3">
            <input type="email" name="email" class="form-control" placeholder="Email" required>
        </div>
        <div class="col-md-2">
            <input type="password" name="password" class="form-control" placeholder="Password" required>
        </div>
        <div class="col-md-2">
            <select name="role" class="form-select" required>
                <option value="">Select Role</option>
                <c:forEach var="role" items="${roles}">
                    <option value="${role}">${role}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2 d-grid">
            <button type="submit" class="btn btn-primary">Add User</button>
        </div>
    </form>

    <!-- Users Table -->
    <div class="card shadow">
        <div class="card-body">
            <table class="table table-hover align-middle">
                <thead class="table-light">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Name</th>
                    <th scope="col">Email</th>
                    <th scope="col">Role</th>
                    <th scope="col">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>${user.role}</td>
                        <td>
                            <div class="d-flex gap-2">
                                <a href="/admin/users/edit/${user.id}" class="btn btn-sm btn-outline-primary">Edit</a>
                                <form action="/admin/users/delete/${user.id}" method="post" onsubmit="return confirm('Are you sure?')">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>
