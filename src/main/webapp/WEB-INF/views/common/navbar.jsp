<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="isBusinessPage" value="${pageContext.session.getAttribute('roleType') == 'business'}" />

<nav class="navbar navbar-expand-lg navbar-light bg-light shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/">JobRecruit</a>

        <sec:authorize access="!isAuthenticated()">
            <div class="form-check form-switch me-3">
                <input class="form-check-input" type="checkbox" id="roleToggle" ${isBusinessPage ? "checked" : ""}>
                <label class="form-check-label" for="roleToggle">
                    <c:choose>
                        <c:when test="${isBusinessPage}">Applicant</c:when>
                        <c:otherwise>Business</c:otherwise>
                    </c:choose>
                </label>
            </div>
        </sec:authorize>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse justify-content-end" id="navbarContent">
            <ul class="navbar-nav mb-2 mb-lg-0">
                <sec:authorize access="!isAuthenticated()">
                    <c:choose>
                        <c:when test="${isBusinessPage}">
                            <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/login">Login</a></li>
                            <li class="nav-item"><a class="btn btn-primary ms-2" href="<%= request.getContextPath() %>/auth/company-signup">Sign up</a></li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/login">Login</a></li>
                            <li class="nav-item"><a class="btn btn-primary ms-2" href="<%= request.getContextPath() %>/auth/signup">Sign up</a></li>
                        </c:otherwise>
                    </c:choose>
                </sec:authorize>

                <sec:authorize access="hasAuthority('COMPANY')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/dashboard">My Company</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/branches">Branches</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/managers">Managers</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/recruiters">Recruiters</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout">Logout</a></li>
                </sec:authorize>


                <sec:authorize access="hasAuthority('APPLICANT')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/profile">My Profile</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout">Logout</a></li>
                </sec:authorize>

                <sec:authorize access="hasAuthority('ADMIN')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/admin/admin-dashboard">Admin</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout">Logout</a></li>
                </sec:authorize>

                <sec:authorize access="hasAuthority('RECRUITER')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/recruiter/interview/feedback">Interview Feedbacks</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/recruiter/interview/list">Interview Schedules</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/profile">My Profile</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout">Logout</a></li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</nav>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const toggle = document.getElementById("roleToggle");
        if (toggle) {
            toggle.addEventListener("change", function () {
                const selectedRole = this.checked ? "business" : "applicant";
                fetch("<%= request.getContextPath() %>/auth/set-role?roleType=" + selectedRole, {
                    method: "POST"
                }).then(() => {
                    window.location.href = "<%= request.getContextPath() %>/";
                });
            });
        }
    });
</script>
