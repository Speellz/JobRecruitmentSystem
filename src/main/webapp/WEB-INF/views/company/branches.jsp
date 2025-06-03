<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Branches</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<div class="page-container">
    <div class="panel business-panel">
        <h2>Manage Branches</h2>

        <form action="${pageContext.request.contextPath}/company/branches/add" method="post" class="form-inline">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="text" name="name" placeholder="Branch Name" required class="input">
            <input type="text" name="location" placeholder="Location" required class="input">
            <button class="button-blue" type="submit">Add Branch</button>
        </form>

        <c:if test="${not empty branches}">
            <ul style="list-style-type: none; padding: 0;">
                <c:forEach var="branch" items="${branches}">
                    <li class="branch-item">
                        <span class="branch-info">${branch.name} - ${branch.location}</span>
                        <div class="branch-actions">
                            <form action="${pageContext.request.contextPath}/company/branches/edit/${branch.id}" method="get" style="display:inline;">
                                <button class="button-blue" type="submit">Edit</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/company/branches/detail/${branch.id}" method="get" style="display:inline;">
                                <button class="button-blue" type="submit">Detail</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/company/branches/delete/${branch.id}" method="post" style="display:inline;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="button-red" type="submit">Delete</button>
                            </form>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </c:if>

        <c:if test="${empty branches}">
            <div class="alert error">No branches found.</div>
        </c:if>
    </div>
</div>

</body>
</html>
