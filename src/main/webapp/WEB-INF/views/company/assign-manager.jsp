<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<head>
    <meta charset="UTF-8">
    <title>Assign Manager</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<div class="page-container">
    <div class="panel company-panel" style="max-width: 600px; margin: 0 auto;">
        <h2 style="text-align: center;">Assign Manager to ${branch.name}</h2>

        <form action="${pageContext.request.contextPath}/company/branches/update-manager/${branch.id}" method="post" style="margin-top: 20px;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <label><strong>Select a recruiter:</strong></label>
            <select name="managerId" class="input" required style="width: 100%; margin-bottom: 20px;">
                <option disabled selected>Select</option>
                <c:forEach var="recruiter" items="${recruiters}">
                    <option value="${recruiter.id}"
                            <c:if test="${branch.manager != null && recruiter.id == branch.manager.id}">selected</c:if>>
                            ${recruiter.user.name} (${recruiter.user.email})
                    </option>
                </c:forEach>
            </select>

            <button type="submit" class="button-blue" style="width: 100%;">Save Manager</button>
        </form>
    </div>
</div>
