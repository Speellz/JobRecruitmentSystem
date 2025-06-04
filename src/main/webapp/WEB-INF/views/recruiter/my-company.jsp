<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>My Company</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="page-container">
    <h1 style="text-align: center;">My Company</h1>

    <div class="panel business-panel" style="max-width: 800px; margin: 0 auto;">
        <h2>🏢 Company Info</h2>
        <p><strong>Name:</strong> ${company.name}</p>
        <p><strong>Email:</strong> ${company.email}</p>
        <p><strong>Phone:</strong> ${company.phone}</p>
        <p><strong>Website:</strong> ${company.website}</p>

        <hr style="margin: 25px 0;">

        <h2>🏬 Branch Info</h2>
        <p><strong>Name:</strong> ${branch.name}</p>
        <p><strong>Location:</strong> ${branch.location}</p>

        <hr style="margin: 25px 0;">

        <h2>👤 Branch Manager</h2>
        <c:if test="${not empty manager}">
            <p><strong>Name:</strong> ${manager.name}</p>
            <p><strong>Email:</strong> ${manager.email}</p>
        </c:if>
        <c:if test="${empty manager}">
            <p>No manager assigned to this branch.</p>
        </c:if>

        <hr style="margin: 25px 0;">
        <h2>👥 Recruiters</h2>

        <c:forEach var="recruiter" items="${recruiters}">
            <div class="panel branch-item" style="margin-bottom: 15px;">
                <p><strong>Name:</strong> ${recruiter.user.name}</p>
                <p><strong>Email:</strong> ${recruiter.user.email}</p>
                <p><strong>Phone:</strong> ${recruiter.phone}</p>

                <c:if test="${isManager}">
                    <form action="${pageContext.request.contextPath}/recruiter/delete/${recruiter.id}" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button class="button-red" type="submit">Delete</button>
                    </form>
                </c:if>
            </div>
        </c:forEach>

        <c:if test="${isManager}">
            <hr style="margin: 25px 0;">
            <h3>Add New Recruiter</h3>
            <form action="${pageContext.request.contextPath}/recruiter/add-by-manager" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <input type="text" name="name" placeholder="Name" required class="input">
                <input type="email" name="email" placeholder="Email" required class="input">
                <input type="text" name="phone" placeholder="Phone" required class="input">
                <input type="password" name="password" placeholder="Password" required class="input">
                <button class="button-blue" type="submit">Add Recruiter</button>
            </form>
        </c:if>

        <div style="margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/recruiter/dashboard" class="button-blue">← Back to Dashboard</a>
        </div>
    </div>
</div>
</body>
</html>
