<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="../common/navbar.jsp" />

<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">

<div class="page-container">
    <h2>Edit User</h2>

    <form action="/admin/users/update" method="post" class="panel">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="id" value="${user.id}" />

        <input type="text" name="name" class="input" placeholder="Name" value="${user.name}" required>
        <input type="email" name="email" class="input" placeholder="Email" value="${user.email}" required>
        <input type="password" name="newPassword" class="input" placeholder="New Password (leave blank to keep old)">

        <select name="role" class="input" required>
            <option value="" disabled>Select Role</option>
            <c:forEach var="role" items="${roles}">
                <option value="${role}" <c:if test="${role == user.role}">selected</c:if>>${role}</option>
            </c:forEach>
        </select>

        <button type="submit" class="button-blue">Update</button>
        <a href="/admin/users" class="button" style="background-color:#6c757d;">Cancel</a>
    </form>
</div>
