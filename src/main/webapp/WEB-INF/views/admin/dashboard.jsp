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

    <h1 style="text-align: center; margin-bottom: 30px;">Admin Dashboard</h1>

    <c:if test="${not empty success}">
        <div class="alert success">${success}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
    </c:if>

    <h2 style="margin-bottom: 20px;">Pending Companies</h2>
    <c:if test="${empty pendingCompanies}">
        <div class="panel" style="text-align: center;">No pending companies to approve.</div>
    </c:if>

    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px;">
        <c:forEach var="company" items="${pendingCompanies}">
            <div class="panel company-panel" style="text-align: left;">
                <p><strong>🧾 Name:</strong> ${company.name}</p>
                <p><strong>📧 Email:</strong> ${company.email}</p>
                <p><strong>📞 Phone:</strong> ${company.phone}</p>
                <p><strong>🌐 Website:</strong> <a href="${company.website}" target="_blank">${company.website}</a></p>

                <div style="margin-top: 15px; display: flex; gap: 10px;">
                    <form action="${pageContext.request.contextPath}/admin/approve-company/${company.id}" method="post">
                        <button type="submit" class="button-blue">Approve</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/admin/reject-company/${company.id}" method="post">
                        <button type="submit" class="button-red">Reject</button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
