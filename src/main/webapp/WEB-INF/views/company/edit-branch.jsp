<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<jsp:include page="../common/navbar.jsp"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Branch</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<div class="page-container">
    <div class="panel business-panel">
        <h2>Edit Branch</h2>

        <form:form method="post" action="${pageContext.request.contextPath}/company/branches/update" modelAttribute="branch">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <form:hidden path="id" />
            <div style="margin-bottom: 15px;">
                <label>Name:</label><br>
                <form:input path="name" cssClass="input" />
            </div>
            <div style="margin-bottom: 15px;">
                <label>Location:</label><br>
                <form:input path="location" cssClass="input" />
            </div>
            <input class="button-blue" type="submit" value="Update">
        </form:form>
    </div>
</div>

</body>
</html>
