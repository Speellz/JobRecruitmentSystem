<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<head>
    <meta charset="UTF-8">
    <title>Recruiters</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>
<div class="page-container">
    <h1 style="text-align:center; margin-bottom:30px;">Recruiters</h1>

    <div style="text-align: right; margin-bottom: 15px;">
        <a href="${pageContext.request.contextPath}/company/recruiters/add" class="button-blue">+ Add Recruiter</a>
    </div>

    <c:choose>
        <c:when test="${not empty branches}">
            <c:forEach var="branch" items="${branches}">
                <h3 style="margin-top: 25px; margin-bottom: 12px;">${branch.name} (${branch.location})</h3>

                <c:set var="found" value="false" />
                <c:forEach var="recruiter" items="${recruiters}">
                    <c:if test="${recruiter.branch.id == branch.id}">
                        <c:set var="found" value="true" />
                        <div class="panel branch-item" style="margin-bottom:10px;">
                            <p><strong>👤 Name:</strong> ${recruiter.user.name}</p>
                            <p><strong>📧 Email:</strong> ${recruiter.user.email}</p>
                            <p><strong>📞 Phone:</strong> ${recruiter.phone}</p>
                            <p><strong>🏢 Branch:</strong> ${recruiter.branch.name} (${recruiter.branch.location})</p>
                            <form action="${pageContext.request.contextPath}/company/recruiters/delete/${recruiter.id}" method="post" style="display:inline;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="button-red" type="submit" onclick="return confirm('Are you sure?');">Delete</button>
                            </form>
                        </div>
                    </c:if>
                </c:forEach>

                <c:if test="${not found}">
                    <div class="panel" style="background-color: #f9f9f9; text-align:center;">No recruiters found.</div>
                </c:if>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="panel" style="background-color: #f9f9f9; text-align:center;">No branches or recruiters found.</div>
        </c:otherwise>
    </c:choose>
</div>
</body>
