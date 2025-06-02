<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<head>
    <meta charset="UTF-8">
    <title>Assign Manager</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="page-container">
    <div class="panel company-panel" style="max-width: 500px; margin: 0 auto;">
        <h2 style="text-align: center;">Assign Manager</h2>
        <form action="${pageContext.request.contextPath}/admin/branches/${branch.id}/assign-manager" method="post">
            <label for="managerRecruiterId"><strong>Select Manager from Branch Recruiters:</strong></label>
            <select id="managerRecruiterId" name="managerRecruiterId" required class="input" style="margin: 16px 0;">
                <option value="" disabled selected>Choose recruiter...</option>
                <c:forEach var="recruiter" items="${recruiters}">
                    <option value="${recruiter.id}">
                            ${recruiter.user.name} (${recruiter.user.email})
                    </option>
                </c:forEach>
            </select>
            <button type="submit" class="button-blue">Assign as Manager</button>
        </form>
        <div style="margin-top: 15px;">
            <a href="${pageContext.request.contextPath}/admin/branches/${branch.id}" class="button">← Back to Branch</a>
        </div>
    </div>
</div>
</body>
