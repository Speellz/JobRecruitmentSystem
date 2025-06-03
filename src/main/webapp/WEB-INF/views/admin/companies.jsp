<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<head>
    <meta charset="UTF-8">
    <title>Approved Companies</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="page-container">
    <h1 style="text-align: center; margin-bottom: 30px;">Approved Companies</h1>

    <c:if test="${empty companies}">
        <div class="panel" style="text-align: center;">No approved companies available.</div>
    </c:if>

    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px;">
        <c:forEach var="company" items="${companies}">
            <div class="panel company-panel" style="text-align: left;">
                <p><strong>🏢 Name:</strong> ${company.name}</p>
                <p><strong>📧 Email:</strong> ${company.email}</p>
                <p><strong>📞 Phone:</strong> ${company.phone}</p>
                <p><strong>🌐 Website:</strong> <a href="${company.website}" target="_blank">${company.website}</a></p>

                <div style="margin-top: 15px;">
                    <a href="${pageContext.request.contextPath}/admin/companies/${company.id}" class="button-blue">View Details</a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
