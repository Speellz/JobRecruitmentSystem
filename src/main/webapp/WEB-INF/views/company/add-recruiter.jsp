<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<head>
    <meta charset="UTF-8">
    <title>Add Recruiter</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="page-container">
    <h1 style="text-align:center; margin-bottom:30px;">Add Recruiter</h1>
    <form action="${pageContext.request.contextPath}/company/recruiters/add" method="post" style="max-width:400px;margin:0 auto;">
        <label for="branchId">Select Branch:</label>
        <select name="branchId" id="branchId" required class="input" style="margin-bottom:15px;">
            <option value="">Choose branch...</option>
            <c:forEach var="branch" items="${branches}">
                <option value="${branch.id}">${branch.name} (${branch.location})</option>
            </c:forEach>
        </select>
        <input type="text" name="user.name" placeholder="Name" required class="input" />
        <input type="email" name="user.email" placeholder="Email" required class="input" />
        <input type="text" name="user.phone" placeholder="Phone" required class="input" />
        <input type="password" name="user.password" placeholder="Password" required class="input" />
        <button type="submit" class="button-blue" style="width:100%;margin-top:16px;">Add Recruiter</button>
    </form>
    <div style="text-align:center;margin-top:15px;">
        <a href="${pageContext.request.contextPath}/company/recruiters" class="button">← Back</a>
    </div>
</div>
</body>
