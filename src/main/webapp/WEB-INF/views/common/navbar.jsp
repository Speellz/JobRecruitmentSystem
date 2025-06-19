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
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/dashboard"><i class="fa-solid fa-building"></i> My Company</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/branches"><i class="fa-solid fa-code-branch"></i> Branches</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/managers"><i class="fa-solid fa-user-tie"></i> Managers</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/recruiters"><i class="fa-solid fa-user-plus"></i> Recruiters</a></li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="notificationDropdown-comp" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-bell"></i>
                            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger d-none" id="notifCount-comp">0</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="notificationDropdown-comp" id="notificationMenu-comp">
                            <li><span class="dropdown-item-text">Loading notifications...</span></li>
                        </ul>
                    </li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </sec:authorize>

                <sec:authorize access="hasAuthority('APPLICANT')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/profile"><i class="fa-solid fa-user"></i> My Profile</a></li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="notificationDropdown-app" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-bell"></i>
                            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger d-none" id="notifCount-app">0</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="notificationDropdown-app" id="notificationMenu-app">
                            <li><span class="dropdown-item-text">Loading notifications...</span></li>
                        </ul>
                    </li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </sec:authorize>

                <sec:authorize access="hasAuthority('ADMIN')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/admin/admin-dashboard"><i class="fa-solid fa-user-shield"></i> Admin</a></li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="notificationDropdown-admin" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-bell"></i>
                            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger d-none" id="notifCount-admin">0</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="notificationDropdown-admin" id="notificationMenu-admin">
                            <li><span class="dropdown-item-text">Loading notifications...</span></li>
                        </ul>
                    </li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </sec:authorize>

                <sec:authorize access="hasAuthority('RECRUITER')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/recruiter/interview/feedback"><i class="fa-solid fa-comments"></i> Interview Feedbacks</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/recruiter/interview/list"><i class="fa-solid fa-calendar-check"></i> Interview Schedules</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/profile"><i class="fa-solid fa-user"></i> My Profile</a></li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="notificationDropdown-recruiter" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-bell"></i>
                            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger d-none" id="notifCount-recruiter">0</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="notificationDropdown-recruiter" id="notificationMenu-recruiter">
                            <li><span class="dropdown-item-text">Loading notifications...</span></li>
                        </ul>
                    </li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
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
                    method: "POST",
                    headers: {
                        "X-CSRF-TOKEN": "${_csrf.token}"
                    }
                }).then(() => {
                    window.location.href = "<%= request.getContextPath() %>/";
                });
            });
        }

        <sec:authorize access="isAuthenticated()">
        // Fetch notifications for all user types
        fetchNotifications();
        </sec:authorize>
    });

    async function fetchNotifications() {
        try {
            console.log("Fetching notifications...");
            const response = await fetch("<%= request.getContextPath() %>/notifications/json");
            
            if (!response.ok) {
                console.error("Error response:", response.status, response.statusText);
                throw new Error("Network response was not ok: " + response.status);
            }
            
            const contentType = response.headers.get("content-type");
            if (!contentType || !contentType.includes("application/json")) {
                console.error("Response is not JSON:", contentType);
                throw new Error("Expected JSON response but got: " + contentType);
            }
            
            const data = await response.json();
            console.log("Notifications received:", data);
            
            // Determine user role to update correct dropdown
            <sec:authorize access="hasAuthority('APPLICANT')">
            updateNotificationDropdown(data, "app");
            </sec:authorize>
            <sec:authorize access="hasAuthority('COMPANY')">
            updateNotificationDropdown(data, "comp");
            </sec:authorize>
            <sec:authorize access="hasAuthority('ADMIN')">
            updateNotificationDropdown(data, "admin");
            </sec:authorize>
            <sec:authorize access="hasAuthority('RECRUITER')">
            updateNotificationDropdown(data, "recruiter");
            </sec:authorize>
        } catch (error) {
            console.error("Error fetching notifications:", error);
            // Show error in dropdown
            <sec:authorize access="hasAuthority('APPLICANT')">
            showErrorInDropdown("app");
            </sec:authorize>
            <sec:authorize access="hasAuthority('COMPANY')">
            showErrorInDropdown("comp");
            </sec:authorize>
            <sec:authorize access="hasAuthority('ADMIN')">
            showErrorInDropdown("admin");
            </sec:authorize>
            <sec:authorize access="hasAuthority('RECRUITER')">
            showErrorInDropdown("recruiter");
            </sec:authorize>
        }
    }
    
    function showErrorInDropdown(role) {
        const menu = document.getElementById(`notificationMenu-${role}`);
        if (menu) {
            menu.innerHTML = '<li><span class="dropdown-item-text text-danger">Error loading notifications</span></li>';
        }
    }
    
    function updateNotificationDropdown(notifications, role) {
        const badge = document.getElementById(`notifCount-${role}`);
        const menu = document.getElementById(`notificationMenu-${role}`);
        
        if (!badge || !menu) {
            console.error(`Elements for role ${role} not found`);
            return;
        }

        menu.innerHTML = "";
        
        if (!notifications || notifications.length === 0) {
            badge.classList.add("d-none");
            menu.innerHTML = '<li><span class="dropdown-item-text">No notifications</span></li>';
            return;
        }

        badge.textContent = notifications.length;
        badge.classList.remove("d-none");

        notifications.forEach(notification => {
            const li = document.createElement("li");
            li.innerHTML = `<span class="dropdown-item small">${notification.message}</span>`;
            menu.appendChild(li);
        });

        menu.innerHTML += `<li><hr class="dropdown-divider"></li>
                           <li><a class="dropdown-item text-center small" href="<%= request.getContextPath() %>/notifications">See all</a></li>`;
    }
</script>