<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Branches</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<div class="container my-5">
    <div class="card shadow-sm">
        <div class="card-body">
            <h3 class="card-title mb-4">Manage Branches</h3>

            <!-- Add Branch Form -->
            <form action="${pageContext.request.contextPath}/company/branches/add" method="post" class="row g-3 align-items-end mb-4">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="col-md-5">
                    <label class="form-label">Branch Name</label>
                    <input type="text" name="name" class="form-control" required>
                </div>
                <div class="col-md-5">
                    <label class="form-label">Location</label>
                    <input type="text" name="location" class="form-control" required>
                </div>
                <div class="col-md-2">
                    <button class="btn btn-primary w-100" type="submit">Add Branch</button>
                </div>
            </form>

            <!-- Branch List -->
            <c:if test="${not empty branches}">
                <ul class="list-group">
                    <c:forEach var="branch" items="${branches}">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span>${branch.name} - ${branch.location}</span>
                            <div class="btn-group" role="group">
                                <form action="${pageContext.request.contextPath}/company/branches/edit/${branch.id}" method="get">
                                    <button class="btn btn-outline-primary btn-sm" type="submit">Edit</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/company/branches/detail/${branch.id}" method="get">
                                    <button class="btn btn-outline-info btn-sm" type="submit">Detail</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/company/branches/delete/${branch.id}" method="post" onsubmit="return confirm('Are you sure?')">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button class="btn btn-outline-danger btn-sm" type="submit">Delete</button>
                                </form>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>

            <c:if test="${empty branches}">
                <div class="alert alert-warning mt-3">No branches found.</div>
            </c:if>
        </div>
    </div>
</div>

</body>
</html>
