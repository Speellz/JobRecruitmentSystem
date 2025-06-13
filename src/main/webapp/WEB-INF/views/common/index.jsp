<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>

<%
    response.setContentType("text/html;charset=UTF-8");
%>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JobRecruit | Home</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>

<jsp:include page="navbar.jsp" />

<c:if test="${not empty error}">
    <div class="alert error">${error}</div>
</c:if>

<c:if test="${not empty success}">
    <div class="alert success">${success}</div>
</c:if>

<div class="page-container">

    <c:choose>
        <c:when test="${not empty pageContext.request.userPrincipal}">
            <sec:authorize access="hasAuthority('ADMIN')">
                <div class="panel admin-panel">
                    <h2>Admin Panel</h2>
                    <p>Manage pending companies and platform settings.</p>
                    <a href="${pageContext.request.contextPath}/admin/admin-dashboard" class="button">Manage Companies</a>
                    <a href="${pageContext.request.contextPath}/admin/users" class="button">Manage Users</a>
                    <a href="${pageContext.request.contextPath}/admin/companies" class="button">View All Companies</a>
                </div>
            </sec:authorize>

            <sec:authorize access="hasAuthority('COMPANY')">
                <c:choose>
                    <c:when test="${empty sessionScope.userCompany}">
                        <div class="panel business-panel">
                            <h2>Register Your Company</h2>
                            <form action="${pageContext.request.contextPath}/company/add-company" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="text" name="name" placeholder="Company Name" required class="input">
                                <input type="email" name="email" placeholder="Company Email" required class="input">
                                <input type="text" name="phone" placeholder="Phone" required class="input">
                                <input type="text" name="website" placeholder="Website" required class="input">
                                <button type="submit" class="button">Submit</button>
                            </form>
                        </div>
                    </c:when>

                    <c:when test="${sessionScope.userCompany.status == 'Pending'}">
                        <div class="panel business-panel">
                            <h2>Your company is awaiting admin approval!</h2>
                            <p>Please wait until an administrator approves your registration.</p>
                        </div>
                    </c:when>

                    <c:when test="${sessionScope.userCompany.status == 'Approved'}">
                        <div class="panel business-panel">
                            <h2>Welcome, ${sessionScope.userCompany.name}!</h2>
                            <p>Manage your company operations.</p>
                            <a href="${pageContext.request.contextPath}/company/branches" class="button">Manage Branches</a>
                            <a href="${pageContext.request.contextPath}/company/managers" class="button">Manage Managers</a>
                            <a href="${pageContext.request.contextPath}/company/recruiters" class="button">Manage Recruiters</a>
                        </div>
                    </c:when>
                </c:choose>
            </sec:authorize>
        </c:when>
    </c:choose>


    <div class="main-column title-panel">
        <h2 style="margin: 0; font-family: 'Poppins', sans-serif; color: #0a66c2;">
            Latest Job Postings
        </h2>

        <sec:authorize access="hasAuthority('RECRUITER')">
            <a href="${pageContext.request.contextPath}/recruiter/job/form" class="button-blue" style="margin-top: 15px; display: inline-block;">+ Add Job</a>
        </sec:authorize>

        <c:if test="${empty jobList}">
            <p style="margin-top: 15px;">No job postings yet.</p>
        </c:if>
    </div>


    <div class="job-card-container">
        <c:forEach var="job" items="${jobList}">
            <div class="job-card">
                <div class="job-header">
                    <div class="recruiter-name">${job.recruiter.user.name}</div>
                    <div class="recruiter-info">
                            ${job.company.name} • ${job.branch.name}
                    </div>
                </div>

                <div class="job-body">
                    <h3>${job.title}</h3>
                    <p>${job.description}</p>

                    <p><strong>Location:</strong> ${job.location}</p>
                    <p><strong>Type:</strong> ${job.employmentType}</p>
                    <p><strong>Salary:</strong> ${job.salaryRange}</p>
                    <p><strong>Created:</strong> ${df:format(job.createdAt)}</p>
                </div>

                <div class="job-actions">
                    <sec:authorize access="hasAuthority('RECRUITER')">
                        <a href="${pageContext.request.contextPath}/recruiter/job/${job.id}/edit" class="view-link">Edit</a>
                    </sec:authorize>

                    <sec:authorize access="hasAuthority('APPLICANT')">
                        <a href="${pageContext.request.contextPath}/job/${job.id}/apply" class="view-link">Apply</a>
                    </sec:authorize>

                    <sec:authorize access="!isAuthenticated()">
                        <a href="${pageContext.request.contextPath}/auth/login" class="view-link">Login to Apply</a>
                    </sec:authorize>
                </div>
            </div>
        </c:forEach>
    </div>

</div>

</body>
</html>