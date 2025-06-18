<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Company Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body class="bg-light">
<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <h1 class="text-center mb-4">Company Details</h1>

    <div class="card shadow-sm mb-4 mx-auto" style="max-width: 600px;">
        <div class="card-body">
            <p><strong>🧾 Name:</strong> ${company.name}</p>
            <p><strong>📧 Email:</strong> ${company.email}</p>
            <p><strong>📞 Phone:</strong> ${company.phone}</p>
            <p><strong>🌐 Website:</strong> <a href="${company.website}" target="_blank">${company.website}</a></p>
            <p><strong>✅ Status:</strong> ${company.status}</p>
            <p><strong>📅 Created At:</strong> ${company.createdAt}</p>

            <c:if test="${not empty company.owner}">
                <hr>
                <h5>Owner Info</h5>
                <p><strong>👤 Name:</strong> ${company.owner.name}</p>
                <p><strong>📧 Email:</strong> ${company.owner.email}</p>
            </c:if>
        </div>
    </div>

    <div class="card shadow-sm mx-auto mb-4" style="max-width: 600px;">
        <div class="card-body">
            <h5 class="mb-3">Add New Branch</h5>
            <form action="${pageContext.request.contextPath}/admin/branches/add?companyId=${company.id}" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                <div class="mb-3">
                    <label class="form-label">Name</label>
                    <input type="text" name="name" class="form-control" required />
                </div>

                <div class="mb-3">
                    <label class="form-label">Location</label>
                    <input type="text" name="location" class="form-control" required />
                </div>

                <div class="text-end">
                    <button type="submit" class="btn btn-primary">Add Branch</button>
                </div>
            </form>
        </div>
    </div>

    <div class="mb-4">
        <h4 class="mb-3">Branches</h4>
        <c:if test="${empty branches}">
            <div class="alert alert-info">No branches found for this company.</div>
        </c:if>

        <div class="row g-3">
            <c:forEach var="branch" items="${branches}">
                <div class="col-md-6">
                    <div class="card h-100">
                        <div class="card-body">
                            <p><strong>Name:</strong> ${branch.name}</p>
                            <p><strong>Location:</strong> ${branch.location}</p>
                        </div>
                        <div class="card-footer d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/admin/branches/${branch.id}" class="btn btn-outline-primary btn-sm">View</a>
                            <form action="${pageContext.request.contextPath}/admin/branches/delete/${branch.id}" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <input type="hidden" name="companyId" value="${company.id}" />
                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?')">Delete</button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>
