<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp"/>

<head>
    <meta charset="UTF-8">
    <title>Branch Details</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<div class="page-container">
    <h1 style="text-align: center; margin-bottom: 30px;">Branch Detail</h1>

    <div class="panel business-panel" style="max-width: 800px; margin: 0 auto; text-align: left;">
        <p><strong>🏢 Name:</strong> ${branch.name}</p>
        <p><strong>📍 Location:</strong> ${branch.location}</p>
        <p><strong>📅 Created At:</strong> ${formattedCreatedAt}</p>
        <p><strong>🏢 Company:</strong> ${branch.company.name}</p>

        <c:if test="${not empty branch.manager}">
            <hr style="margin: 25px 0;">
            <h3>Branch Manager</h3>
            <p><strong>👤 Name:</strong> ${branch.manager.name}</p>
            <p><strong>📧 Email:</strong> ${branch.manager.email}</p>
        </c:if>

        <hr style="margin: 25px 0;">
        <h3>Recruiters in this Branch</h3>

        <c:choose>
            <c:when test="${not empty recruiters}">
                <c:forEach var="recruiter" items="${recruiters}">
                    <div class="panel branch-item" style="margin-bottom: 15px;">
                        <p><strong>👤 Name:</strong> ${recruiter.user.name}</p>
                        <p><strong>📧 Email:</strong> ${recruiter.user.email}</p>
                        <p><strong>📞 Phone:</strong> ${recruiter.user.phone}</p>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="panel">No recruiters found in this branch.</div>
            </c:otherwise>
        </c:choose>

        <div style="margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/company/branches" class="button-blue">← Back to Branches</a>
        </div>
    </div>
</div>

</body>
