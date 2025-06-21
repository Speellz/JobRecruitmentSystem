<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="notifCount" value="${fn:length(notifications)}" />

<c:set var="isBusinessPage" value="${pageContext.session.getAttribute('roleType') == 'business'}" />
<sec:csrfInput />

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

                <!-- COMPANY -->
                <sec:authorize access="hasAuthority('COMPANY')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/dashboard"><i class="fa-solid fa-building"></i> My Company</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/branches"><i class="fa-solid fa-code-branch"></i> Branches</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/managers"><i class="fa-solid fa-user-tie"></i> Managers</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/company/recruiters"><i class="fa-solid fa-user-plus"></i> Recruiters</a></li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="notificationDropdown-comp" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-bell"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end p-2 shadow-sm"
                            aria-labelledby="notificationDropdown-comp"
                            id="notificationMenu-comp"
                            style="min-width: 300px; max-height: 400px; overflow-y: auto;">
                            <c:forEach var="n" items="${notifications}">
                                <li class="mb-2">
                                    <div class="dropdown-item d-flex align-items-start">
                                        <i class="fa-solid fa-envelope text-primary me-2 mt-1"></i>
                                        <div class="small text-wrap">${n.message}</div>
                                    </div>
                                </li>
                            </c:forEach>
                            <c:if test="${empty notifications}">
                                <li><span class="dropdown-item text-muted">No notifications</span></li>
                            </c:if>
                        </ul>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="messageDropdown-comp" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-envelope"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end p-2 shadow-sm"
                            aria-labelledby="messageDropdown-comp"
                            id="messageMenu-comp"
                            style="min-width: 300px; max-height: 400px; overflow-y: auto;">
                            <c:forEach var="msg" items="${latestMessages}">
                                <li class="mb-2">
                                    <a class="dropdown-item d-flex align-items-start chat-link" href="#" data-chat-url="${pageContext.request.contextPath}/messages/application/${msg.application.id}">
                                        <i class="fa-solid fa-comment-dots text-primary me-2 mt-1"></i>
                                        <div>
                                            <div class="fw-bold">${msg.sender.name}</div>
                                            <div class="small text-muted">${fn:substring(msg.messageContent, 0, 40)}...</div>
                                        </div>
                                    </a>
                                </li>
                            </c:forEach>
                            <c:if test="${empty latestMessages}">
                                <li><span class="dropdown-item text-muted">No messages</span></li>
                            </c:if>
                        </ul>
                    </li>

                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </sec:authorize>

                <!-- APPLICANT -->
                <sec:authorize access="hasAuthority('APPLICANT')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/applicant/company/feedback"><i class="fa-solid fa-star"></i> Company Feedback</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/profile"><i class="fa-solid fa-user"></i> My Profile</a></li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="notificationDropdown-app" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-bell"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end p-2 shadow-sm"
                            aria-labelledby="notificationDropdown-app"
                            id="notificationMenu-app"
                            style="min-width: 300px; max-height: 400px; overflow-y: auto;">
                            <c:forEach var="n" items="${notifications}">
                                <li class="mb-2">
                                    <div class="dropdown-item d-flex align-items-start">
                                        <i class="fa-solid fa-envelope text-primary me-2 mt-1"></i>
                                        <div class="small text-wrap">${n.message}</div>
                                    </div>
                                </li>
                            </c:forEach>
                            <c:if test="${empty notifications}">
                                <li><span class="dropdown-item text-muted">No notifications</span></li>
                            </c:if>
                        </ul>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="messageDropdown-app" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-envelope"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end p-2 shadow-sm"
                            aria-labelledby="messageDropdown-app"
                            id="messageMenu-app"
                            style="min-width: 300px; max-height: 400px; overflow-y: auto;">
                            <c:forEach var="msg" items="${latestMessages}">
                                <li class="mb-2">
                                    <a class="dropdown-item d-flex align-items-start chat-link" href="#" data-chat-url="${pageContext.request.contextPath}/messages/application/${msg.application.id}">
                                        <i class="fa-solid fa-comment-dots text-primary me-2 mt-1"></i>
                                        <div>
                                            <div class="fw-bold">${msg.sender.name}</div>
                                            <div class="small text-muted">${fn:substring(msg.messageContent, 0, 40)}...</div>
                                        </div>
                                    </a>
                                </li>
                            </c:forEach>
                            <c:if test="${empty latestMessages}">
                                <li><span class="dropdown-item text-muted">No messages</span></li>
                            </c:if>
                        </ul>
                    </li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </sec:authorize>

                <!-- ADMIN -->
                <sec:authorize access="hasAuthority('ADMIN')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/admin/admin-dashboard"><i class="fa-solid fa-user-shield"></i> Admin</a></li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="notificationDropdown-admin" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-bell"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end p-2 shadow-sm"
                            aria-labelledby="notificationDropdown-admin"
                            id="notificationMenu-admin"
                            style="min-width: 300px; max-height: 400px; overflow-y: auto;">
                            <c:forEach var="n" items="${notifications}">
                                <li class="mb-2">
                                    <div class="dropdown-item d-flex align-items-start">
                                        <i class="fa-solid fa-envelope text-primary me-2 mt-1"></i>
                                        <div class="small text-wrap">${n.message}</div>
                                    </div>
                                </li>
                            </c:forEach>
                            <c:if test="${empty notifications}">
                                <li><span class="dropdown-item text-muted">No notifications</span></li>
                            </c:if>
                        </ul>
                    </li>

                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </sec:authorize>

                <!-- RECRUITER -->
                <sec:authorize access="hasAuthority('RECRUITER')">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/recruiter/interview/feedback"><i class="fa-solid fa-comments"></i> Interview Feedbacks</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/recruiter/interview/list"><i class="fa-solid fa-calendar-check"></i> Interview Schedules</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/profile"><i class="fa-solid fa-user"></i> My Profile</a></li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="notificationDropdown-recruiter" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-bell"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end p-2 shadow-sm"
                            aria-labelledby="notificationDropdown-recruiter"
                            id="notificationMenu-recruiter"
                            style="min-width: 300px; max-height: 400px; overflow-y: auto;">
                            <c:forEach var="n" items="${notifications}">
                                <li class="mb-2">
                                    <div class="dropdown-item d-flex align-items-start">
                                        <i class="fa-solid fa-envelope text-primary me-2 mt-1"></i>
                                        <div class="small text-wrap">${n.message}</div>
                                    </div>
                                </li>
                            </c:forEach>
                            <c:if test="${empty notifications}">
                                <li><span class="dropdown-item text-muted">No notifications</span></li>
                            </c:if>
                        </ul>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle position-relative" href="#" id="messageDropdown-recruiter" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-envelope"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end p-2 shadow-sm"
                            aria-labelledby="messageDropdown-recruiter"
                            id="messageMenu-recruiter"
                            style="min-width: 300px; max-height: 400px; overflow-y: auto;">
                            <c:forEach var="msg" items="${latestMessages}">
                                <li class="mb-2">
                                    <a class="dropdown-item d-flex align-items-start chat-link" href="#" data-chat-url="${pageContext.request.contextPath}/messages/application/${msg.application.id}">
                                        <i class="fa-solid fa-comment-dots text-primary me-2 mt-1"></i>
                                        <div>
                                            <div class="fw-bold">${msg.sender.name}</div>
                                            <div class="small text-muted">${fn:substring(msg.messageContent, 0, 40)}...</div>
                                        </div>
                                    </a>
                                </li>
                            </c:forEach>
                            <c:if test="${empty latestMessages}">
                                <li><span class="dropdown-item text-muted">No messages</span></li>
                            </c:if>
                        </ul>
                    </li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/auth/logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </sec:authorize>

            </ul>
        </div>
    </div>
</nav>

<!-- Chat Modal -->
<div class="modal fade" id="chatModal" tabindex="-1" aria-labelledby="chatModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="chatModalLabel">Chat</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-0">
                <iframe id="chatIframe" src="" style="width:100%;height:70vh;border:0;"></iframe>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.chat-link').forEach(function(el) {
        el.addEventListener('click', function (e) {
            e.preventDefault();
            var url = this.getAttribute('data-chat-url');
            var iframe = document.getElementById('chatIframe');
            iframe.src = url;
            var modal = new bootstrap.Modal(document.getElementById('chatModal'));
            modal.show();
        });
    });
});
</script>
