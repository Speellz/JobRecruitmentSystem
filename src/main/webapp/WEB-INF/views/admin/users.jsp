<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="../common/navbar.jsp" />

<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">

<div class="page-container">
    <h2>Users</h2>

    <form action="/admin/users/create" method="post"
          style="display: flex; flex-wrap: wrap; gap: 10px; align-items: center;">
        <input type="text" name="name" placeholder="Name" class="input" style="flex: 1 1 200px;">
        <input type="email" name="email" placeholder="Email" class="input" style="flex: 1 1 200px;">
        <input type="password" name="password" placeholder="Password" class="input" style="flex: 1 1 200px;">
        <select name="role" class="input" style="flex: 1 1 200px;">
            <option value="">Select Role</option>
            <c:forEach var="role" items="${roles}">
                <option value="${role}">${role}</option>
            </c:forEach>
        </select>
        <button type="submit" class="button-blue"
                style="flex: 1 1 120px; height: 40px; padding: 6px 12px; margin: 0;">Add User</button>
    </form>


    <div class="panel">
        <table style="width:100%; border-collapse:collapse;">
            <thead>
            <tr style="background-color:#eee;">
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}">
                <tr style="text-align:center;">
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>
                        <a href="/admin/users/edit/${user.id}" class="button-blue">Edit</a>
                        <form action="/admin/users/delete/${user.id}" method="post" style="display:inline;">
                            <button type="submit" class="button-red" onclick="return confirm('Are you sure?')">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
