<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<head>
    <meta charset="UTF-8">
    <title>Branch Details</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>
<div class="page-container">
    <h1 style="text-align: center; margin-bottom: 30px;">Branch Detail</h1>
    <div class="panel company-panel" style="max-width: 800px; margin: 0 auto; text-align: left;">
        <p><strong>🏢 Name:</strong> ${branch.name}</p>
        <p><strong>📍 Location:</strong> ${branch.location}</p>

        <hr style="margin: 25px 0;">

        <h3 style="margin-bottom: 10px;">Manager</h3>
        <c:choose>
            <c:when test="${not empty branch.manager}">
                <div class="panel branch-item" style="margin-bottom: 10px;">
                    <p><strong>👤 Name:</strong> ${branch.manager.name}</p>
                    <p><strong>📧 Email:</strong> ${branch.manager.email}</p>
                    <p><strong>📞 Phone:</strong> ${branch.manager.phone}</p>
                    <a href="${pageContext.request.contextPath}/admin/branches/${branch.id}/edit-manager" class="button-blue" style="margin-top:8px;">Edit Manager</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="panel" style="background-color: #fff3cd; color: #856404; padding: 10px 15px; border-left: 4px solid #ffc107; margin-bottom:10px; display: flex; align-items: center; justify-content: space-between;">
                    <span>No manager assigned to this branch.</span>
                    <a href="${pageContext.request.contextPath}/admin/branches/${branch.id}/assign-manager" class="button-blue" style="margin-left: 10px;">Assign Manager</a>
                </div>
            </c:otherwise>
        </c:choose>

        <hr style="margin: 25px 0;">

        <h3 style="margin-bottom: 15px;">Recruiters in this Branch</h3>
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
                <div class="panel" style="background-color: #f9f9f9;">
                    No recruiters found in this branch.
                </div>
            </c:otherwise>
        </c:choose>

        <div style="margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/admin/companies/${branch.company.id}" class="button-blue">← Back to Company</a>
        </div>
    </div>
</div>
</body>
