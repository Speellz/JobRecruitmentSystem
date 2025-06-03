<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JobRecruit | Home</title>
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
                        <div class="panel company-add">
                            <h2>Register Your Company</h2>
                            <form action="${pageContext.request.contextPath}/company/add-company" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="text" name="name" placeholder="Company Name" required>
                                <input type="email" name="email" placeholder="Company Email" required>
                                <input type="text" name="phone" placeholder="Phone" required>
                                <input type="text" name="website" placeholder="Website" required>
                                <button type="submit">Submit</button>
                            </form>
                        </div>
                    </c:when>

                    <c:when test="${sessionScope.userCompany.status == 'Pending'}">
                        <div class="panel company-pending">
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

            <sec:authorize access="hasAuthority('RECRUITER')">
                <div class="panel recruiter-panel">
                    <h2>Recruiter Panel</h2>
                    <p>View and manage your job postings.</p>
                    <a href="${pageContext.request.contextPath}/recruiter/jobs" class="button">View Jobs</a>

                    <h3 style="margin-top:30px;">My Company</h3>
                    <c:choose>
                        <c:when test="${not empty sessionScope.userCompany}">
                            <div class="company-info" style="text-align:left; max-width:600px; margin:0 auto;">
                                <p><strong>🧾 Name:</strong> ${sessionScope.userCompany.name}</p>
                                <p><strong>📧 Email:</strong> ${sessionScope.userCompany.email}</p>
                                <p><strong>📞 Phone:</strong> ${sessionScope.userCompany.phone}</p>
                                <p><strong>🌐 Website:</strong> <a href="${sessionScope.userCompany.website}" target="_blank">${sessionScope.userCompany.website}</a></p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert error">No company information available.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </sec:authorize>

            <sec:authorize access="hasAuthority('APPLICANT')">
                <div class="panel applicant-panel">
                    <h2>Applicant Panel</h2>
                    <p>Find your dream job today!</p>
                    <a href="${pageContext.request.contextPath}/jobs" class="button">Browse Jobs</a>
                </div>
            </sec:authorize>

        </c:when>

        <c:otherwise>
            <c:choose>
                <c:when test="${sessionScope.roleType == 'business'}">
                    <div class="panel business-home">
                        <h2>Welcome Businesses!</h2>
                        <p>Recruit talents and grow your business.</p>
                        <a href="${pageContext.request.contextPath}/auth/login" class="button">Business Login</a>
                        <a href="${pageContext.request.contextPath}/auth/business-signup" class="button">Business Sign Up</a>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="panel applicant-home">
                        <h2>Welcome to JobRecruit!</h2>
                        <p>Find your dream job today!</p>
                        <a href="${pageContext.request.contextPath}/login" class="button">Login</a>
                        <a href="${pageContext.request.contextPath}/signup" class="button">Sign Up</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:otherwise>

    </c:choose>

</div>

</body>
</html>
