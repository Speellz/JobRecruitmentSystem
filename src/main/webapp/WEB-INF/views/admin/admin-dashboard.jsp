<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<jsp:include page="../common/navbar.jsp" />

<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>

<div class="page-container">

    <h1>Admin Dashboard</h1>

    <!-- Başarı veya hata mesajı -->
    <c:if test="${not empty success}">
        <div class="alert success">${success}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
    </c:if>

    <!-- Pending Companies Listesi -->
    <h2>Pending Companies</h2>
    <c:if test="${empty pendingCompanies}">
        <p>No pending companies to approve.</p>
    </c:if>

    <c:forEach var="company" items="${pendingCompanies}">
        <div class="panel company-panel">
            <p><strong>Name:</strong> ${company.name}</p>
            <p><strong>Email:</strong> ${company.email}</p>
            <p><strong>Phone:</strong> ${company.phone}</p>
            <p><strong>Website:</strong> ${company.website}</p>

            <form action="${pageContext.request.contextPath}/admin/approve-company/${company.id}" method="post" style="display: inline;">
                <button type="submit" class="button approve-btn">Approve</button>
            </form>

            <form action="${pageContext.request.contextPath}/admin/reject-company/${company.id}" method="post" style="display: inline;">
                <button type="submit" class="button reject-btn">Reject</button>
            </form>
        </div>
    </c:forEach>

</div>

</body>
