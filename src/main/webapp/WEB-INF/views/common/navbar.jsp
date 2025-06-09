<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="isBusinessPage" value="${pageContext.session.getAttribute('roleType') == 'business'}" />

<nav class="navbar">
    <div class="navbar-left">
        <a href="<%= request.getContextPath() %>/">JobRecruit</a>

        <sec:authorize access="!isAuthenticated()">
            <div class="toggle-switch">
                <input type="checkbox" id="roleToggle" ${isBusinessPage ? "checked" : ""}>
                <label for="roleToggle">
                    <span class="toggle-track">
                        <span class="toggle-option left">Applicant</span>
                        <span class="toggle-option right">Business</span>
                        <span class="toggle-thumb"></span>
                    </span>
                </label>
            </div>
        </sec:authorize>
    </div>

    <div class="navbar-right">
        <sec:authorize access="!isAuthenticated()">
            <c:choose>
                <c:when test="${isBusinessPage}">
                    <a href="<%= request.getContextPath() %>/auth/login">Login</a>
                    <a href="<%= request.getContextPath() %>/auth/company-signup" class="signup-btn">Sign up</a>
                </c:when>
                <c:otherwise>
                    <a href="<%= request.getContextPath() %>/auth/login">Login</a>
                    <a href="<%= request.getContextPath() %>/auth/signup" class="signup-btn">Sign up</a>
                </c:otherwise>
            </c:choose>
        </sec:authorize>

        <sec:authorize access="hasAuthority('COMPANY')">
            <a href="<%= request.getContextPath() %>/company/dashboard">Company Dashboard</a>
            <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
        </sec:authorize>

        <sec:authorize access="hasAuthority('RECRUITER')">
            <a href="<%= request.getContextPath() %>/recruiter/my-company">My Company</a>
            <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
        </sec:authorize>

        <sec:authorize access="hasAuthority('APPLICANT')">
            <a href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
            <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
        </sec:authorize>

        <sec:authorize access="hasAuthority('ADMIN')">
            <a href="<%= request.getContextPath() %>/admin/admin-dashboard">Admin</a>
            <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
        </sec:authorize>
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
