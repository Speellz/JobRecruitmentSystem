<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<head>
    <meta charset="UTF-8">
    <title>Company Details</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>
<div class="page-container">
    <h1 style="text-align: center; margin-bottom: 30px;">Company Detail</h1>

    <div class="panel company-panel" style="max-width: 600px; margin: 0 auto; text-align: left;">
        <p><strong>🧾 Name:</strong> ${company.name}</p>
        <p><strong>📧 Email:</strong> ${company.email}</p>
        <p><strong>📞 Phone:</strong> ${company.phone}</p>
        <p><strong>🌐 Website:</strong> <a href="${company.website}" target="_blank">${company.website}</a></p>
        <p><strong>✅ Status:</strong> ${company.status}</p>
        <p><strong>📅 Created At:</strong> ${company.createdAt}</p>

        <c:if test="${not empty company.owner}">
            <hr>
            <h3>Owner Info</h3>
            <p><strong>👤 Name:</strong> ${company.owner.name}</p>
            <p><strong>📧 Email:</strong> ${company.owner.email}</p>
        </c:if>
    </div>
    <div class="panel company-panel" style="max-width: 600px; margin: 0 auto; text-align: left;">
        <h3 style="margin-top: 30px;">Branches</h3>
        <c:if test="${empty branches}">
            <div class="panel">No branches found for this company.</div>
        </c:if>

        <div class="panel company-panel" style="max-width: 600px; margin: 30px auto; text-align: center;">
            <h3>Add New Branch</h3>
            <form action="${pageContext.request.contextPath}/admin/branches/add?companyId=${company.id}" method="post" style="text-align: left;">

                <div style="margin-bottom: 10px; display: flex; align-items: center;">
                    <label style="width: 100px;">Name:</label>
                    <input type="text" name="name" class="input" required style="flex: 1;"/>
                </div>

                <div style="margin-bottom: 10px; display: flex; align-items: center;">
                    <label style="width: 100px;">Location:</label>
                    <input type="text" name="location" class="input" required style="flex: 1;"/>
                </div>

                <div style="text-align: center;">
                    <button type="submit" class="button-blue">Add Branch</button>
                </div>
            </form>
        </div>

        <c:forEach var="branch" items="${branches}">
            <div class="panel branch-item">
                <div class="branch-info">
                    <p><strong>Name:</strong> ${branch.name}</p>
                    <p><strong>Location:</strong> ${branch.location}</p>
                </div>
                <div class="branch-actions" style="display: flex; gap: 10px; align-items: center;">
                    <a href="${pageContext.request.contextPath}/admin/branches/${branch.id}" class="button-blue">View</a>
                    <form action="${pageContext.request.contextPath}/admin/branches/delete/${branch.id}" method="post" style="margin: 0;">
                        <input type="hidden" name="companyId" value="${company.id}"/>
                        <button type="submit" class="button-red">Delete</button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>
    </div>
</body>
