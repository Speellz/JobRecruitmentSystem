<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit User</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp" />

<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <div class="card shadow">
        <div class="card-body">
            <h3 class="card-title mb-4">Edit User</h3>

            <form action="/admin/users/update" method="post" class="row g-3">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <input type="hidden" name="id" value="${user.id}" />

                <div class="col-md-6">
                    <label class="form-label">Name</label>
                    <input type="text" name="name" class="form-control" value="${user.name}" required>
                </div>

                <div class="col-md-6">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" value="${user.email}" required>
                </div>

                <div class="col-md-6">
                    <label class="form-label">New Password (leave blank to keep current)</label>
                    <input type="password" name="newPassword" class="form-control" placeholder="New Password">
                </div>

                <div class="col-md-6">
                    <label class="form-label">Role</label>
                    <select name="role" class="form-select" required>
                        <option value="" disabled>Select Role</option>
                        <c:forEach var="role" items="${roles}">
                            <option value="${role}" <c:if test="${role == user.role}">selected</c:if>>${role}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="col-12 d-flex gap-3 mt-4">
                    <button type="submit" class="btn btn-primary">Update</button>
                    <a href="/admin/users" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
