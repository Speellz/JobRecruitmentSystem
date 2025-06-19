<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Approved Companies</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>

<body class="bg-light">
<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <h1 class="text-center mb-4">Approved Companies</h1>

    <c:if test="${empty companies}">
        <div class="alert alert-info text-center">No approved companies available.</div>
    </c:if>

    <div class="row g-4">
        <c:forEach var="company" items="${companies}">
            <div class="col-md-6 col-lg-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body">
                        <p><strong>🏢 Name:</strong> ${company.name}</p>
                        <p><strong>📧 Email:</strong> ${company.email}</p>
                        <p><strong>📞 Phone:</strong> ${company.phone}</p>
                        <p><strong>🌐 Website:</strong> <a href="${company.website}" target="_blank">${company.website}</a></p>
                    </div>
                    <div class="card-footer text-end">
                        <a href="${pageContext.request.contextPath}/admin/companies/${company.id}" class="btn btn-primary btn-sm">View Details</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
