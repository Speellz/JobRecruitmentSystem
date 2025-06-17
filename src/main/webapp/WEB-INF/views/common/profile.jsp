<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="com.jobrecruitment.controller.common.DateFormatterUtil" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .profile-header {
            display: flex;
            align-items: center;
            gap: 20px;
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }
        .profile-header img {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            object-fit: cover;
        }
        .profile-details {
            display: flex;
            flex-direction: column;
        }
        .profile-details h2 {
            font-size: 24px;
            margin: 0;
        }
        .profile-details p {
            font-size: 16px;
            color: #555;
            margin: 3px 0;
        }
        .job-card {
            background: #fff;
            padding: 20px;
            margin: 20px 0;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }
        .job-card h3 {
            font-size: 20px;
        }
        .job-card p, .job-card li, .job-card input {
            font-size: 15px;
        }
        .skill-list form {
            display: inline-block;
            margin-right: 10px;
        }
        .skill-list input[type="text"] {
            padding: 5px;
            margin-right: 5px;
        }
    </style>
</head>
<body>
<jsp:include page="navbar.jsp" />

<div class="page-container">
    <div class="profile-header">
        <img src="<%= request.getContextPath() %>/img/default-profile.png" alt="Profile Image">
        <div class="profile-details">
            <h2>${user.name}</h2>
            <p>${user.email}</p>
            <p>Role: ${user.role}</p>
        </div>
    </div>

    <div class="job-card-container" style="margin-top: 30px;">
        <c:if test="${user.role eq 'RECRUITER'}">
            <div class="job-card">
                <h3>Recruiter Information</h3>
                <p><strong>Title:</strong>
                    <c:choose>
                        <c:when test="${recruiter.role eq 'HR_MANAGER'}">Manager</c:when>
                        <c:otherwise>Recruiter</c:otherwise>
                    </c:choose>
                </p>
                <p><strong>Company:</strong> ${recruiter.company.name}</p>
                <p><strong>Branch:</strong> ${recruiter.branch.name}</p>
                <p><strong>Phone:</strong> ${recruiter.phone}</p>
                <p><strong>Joined:</strong> <%= DateFormatterUtil.format((java.time.LocalDateTime) request.getAttribute("recruiter.createdAt")) %></p>
            </div>

            <div class="job-card">
                <h3>Recruiter Analytics</h3>
                <p><strong>Total Job Posts:</strong> ${analytics.totalPosts}</p>
                <p><strong>Average Response Time:</strong> ${analytics.averageHiringTime} days</p>
                <p><strong>Applications Reviewed:</strong> ${analytics.totalHires}</p>
            </div>
        </c:if>

        <c:if test="${user.role eq 'APPLICANT'}">
            <div class="job-card">
                <h3>Education</h3>
                <ul class="skill-list">
                    <c:forEach var="edu" items="${educationList}">
                        <li>${edu.schoolName} - ${edu.degree} (${edu.startDate} - ${edu.endDate})</li>
                    </c:forEach>
                </ul>
            </div>

            <div class="job-card">
                <h3>Employment History</h3>
                <ul class="skill-list">
                    <c:forEach var="emp" items="${employmentList}">
                        <li>${emp.companyName} - ${emp.jobTitle} (${emp.startDate} - ${emp.endDate})</li>
                    </c:forEach>
                </ul>
            </div>

            <div class="job-card">
                <h3>Certifications</h3>
                <ul class="skill-list">
                    <c:forEach var="cert" items="${certificationList}">
                        <li>${cert.certificationName} (${cert.issuedBy})</li>
                    </c:forEach>
                </ul>
            </div>

            <div class="job-card">
                <h3>Skills</h3>
                <form method="post" action="${pageContext.request.contextPath}/skills/add">
                    <input type="text" name="skillName" placeholder="Add new skill" required>
                    <button type="submit">Add</button>
                </form>
                <ul class="skill-list">
                    <c:forEach var="skill" items="${skillList}">
                        <li>
                            <form method="post" action="${pageContext.request.contextPath}/skills/update/${skill.id}" style="display:inline;">
                                <input type="text" name="skillName" value="${skill.skillName}" required>
                                <button type="submit">Update</button>
                            </form>
                            <form method="post" action="${pageContext.request.contextPath}/skills/delete/${skill.id}" style="display:inline;">
                                <button type="submit">Delete</button>
                            </form>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
    </div>
</div>

</body>
</html>
