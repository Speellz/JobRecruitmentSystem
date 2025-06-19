<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Managers</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<div class="container my-5">
    <div class="card shadow-sm p-4">
        <h3 class="mb-4">Manage Managers</h3>

        <c:if test="${not empty branches}">
            <c:forEach var="branch" items="${branches}">
                <div class="card mb-3">
                    <div class="card-body d-flex justify-content-between align-items-center flex-wrap">
                        <div>
                            <h5 class="card-title mb-1">${branch.name} - ${branch.location}</h5>
                            <p class="mb-0">
                                <c:choose>
                                    <c:when test="${not empty branch.manager}">
                                        👤 <strong>${branch.manager.name}</strong> (${branch.manager.email})
                                    </c:when>
                                    <c:otherwise>
                                        <em class="text-muted">No manager assigned</em>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="d-flex gap-2 mt-3 mt-md-0">
                            <form action="${pageContext.request.contextPath}/company/assign-manager/${branch.id}" method="get">
                                <button type="submit" class="btn btn-outline-primary">
                                    <c:choose>
                                        <c:when test="${not empty branch.manager}">Edit</c:when>
                                        <c:otherwise>➕ Add Manager</c:otherwise>
                                    </c:choose>
                                </button>
                            </form>
                            <c:if test="${not empty branch.manager}">
                                <form action="${pageContext.request.contextPath}/company/remove-manager/${branch.id}" method="post">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-outline-danger">Remove</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${empty branches}">
            <div class="alert alert-warning">No branches found.</div>
        </c:if>
    </div>
</div>

</body>
</html>
