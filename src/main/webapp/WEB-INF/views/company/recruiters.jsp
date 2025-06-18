<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Recruiters</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">
<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Recruiters</h2>
        <a href="${pageContext.request.contextPath}/company/recruiters/add" class="btn btn-primary">+ Add Recruiter</a>
    </div>

    <c:choose>
        <c:when test="${not empty branches}">
            <c:forEach var="branch" items="${branches}">
                <h5 class="mt-4 mb-3 text-primary">${branch.name} (${branch.location})</h5>

                <c:set var="found" value="false"/>
                <div class="row">
                    <c:forEach var="recruiter" items="${recruiters}">
                        <c:if test="${recruiter.branch.id == branch.id}">
                            <c:set var="found" value="true"/>
                            <div class="col-md-6 mb-3">
                                <div class="card shadow-sm">
                                    <div class="card-body">
                                        <h5 class="card-title mb-2">${recruiter.user.name}</h5>
                                        <p class="mb-1"><strong>Email:</strong> ${recruiter.user.email}</p>
                                        <p class="mb-1"><strong>Phone:</strong> ${recruiter.phone}</p>
                                        <p class="mb-3"><strong>Branch:</strong> ${recruiter.branch.name} (${recruiter.branch.location})</p>
                                        <form action="${pageContext.request.contextPath}/company/recruiters/delete/${recruiter.id}" method="post" onsubmit="return confirm('Are you sure?');">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>

                <c:if test="${not found}">
                    <div class="alert alert-warning">No recruiters found in this branch.</div>
                </c:if>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="alert alert-secondary text-center">No branches or recruiters found.</div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
