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
    <h2>Manage Branches</h2>

    <form action="${pageContext.request.contextPath}/company/branches/add" method="post" class="form-inline">
        <input type="text" name="name" placeholder="Branch Name" required>
        <input type="text" name="location" placeholder="Location" required>
        <button type="submit">Add Branch</button>
    </form>

    <c:if test="${not empty branches}">
        <ul>
            <c:forEach var="branch" items="${branches}">
                <li>
                        ${branch.name} - ${branch.location}
                    <form action="${pageContext.request.contextPath}/company/branches/delete/${branch.id}" method="post" style="display:inline;">
                        <button type="submit">Delete</button>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${empty branches}">
        <p>No branches found.</p>
    </c:if>
</div>

</body>
</html>
