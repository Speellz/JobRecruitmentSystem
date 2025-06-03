<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Managers</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .manager-card {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 16px;
            margin-bottom: 15px;
            border-radius: 10px;
            background-color: #fff;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        .manager-info {
            font-size: 16px;
        }
        .manager-info em {
            color: #888;
        }
        .manager-actions form {
            display: inline-block;
            margin-left: 6px;
        }
    </style>
</head>
<body>

<div class="page-container">
    <div class="panel business-panel">
        <h2>Manage Managers</h2>

        <c:if test="${not empty branches}">
            <c:forEach var="branch" items="${branches}">
                <div class="manager-card">
                    <div class="manager-info">
                        <strong>${branch.name}</strong> - ${branch.location}
                        <br/>
                        <c:choose>
                            <c:when test="${not empty branch.manager}">
                                👤 <strong>${branch.manager.name}</strong> (${branch.manager.email})
                            </c:when>
                            <c:otherwise>
                                <em>No manager assigned</em>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="manager-actions">
                        <c:choose>
                            <c:when test="${not empty branch.manager}">
                                <form action="${pageContext.request.contextPath}/company/assign-manager/${branch.id}" method="get">
                                    <button class="button-blue" type="submit">Edit</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/company/remove-manager/${branch.id}" method="post">
                                    <button class="button-red" type="submit">Remove</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form action="${pageContext.request.contextPath}/company/assign-manager/${branch.id}" method="get">
                                    <button class="button-blue" type="submit">➕ Add Manager</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${empty branches}">
            <div class="alert error">No branches found.</div>
        </c:if>
    </div>
</div>

</body>
</html>
