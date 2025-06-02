<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Managers</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<div class="page-container">
    <div class="panel business-panel">
        <h2>Manage Managers</h2>

        <c:if test="${not empty branches}">
            <ul style="list-style-type: none; padding: 0;">
                <c:forEach var="branch" items="${branches}">
                    <li class="branch-item">
                        <span class="branch-info">
                            ${branch.name} - ${branch.location}
                            <c:choose>
                                <c:when test="${not empty branch.manager}">
                                    <br>
                                    👤 <strong>${branch.manager.name}</strong> (${branch.manager.email})
                                </c:when>
                                <c:otherwise>
                                    <br>
                                    <em>No manager assigned</em>
                                </c:otherwise>
                            </c:choose>
                        </span>

                        <div class="branch-actions">
                            <c:choose>
                                <c:when test="${not empty branch.manager}">
                                    <form action="${pageContext.request.contextPath}/company/branches/update-manager/${branch.id}" method="get" style="display:inline;">
                                        <button class="button-blue" type="submit">Edit</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/company/branches/remove-manager/${branch.id}" method="post" style="display:inline;">
                                        <button class="button-red" type="submit">Remove</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/company/assign-manager/${branch.id}" method="get" style="display:inline;">
                                        <button class="button-blue" type="submit">➕ Add Manager</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
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
