<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
    <style>
        body {
            font-family: Arial, sans-serif;
            font-size: 14px;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
        }

        .chat-container {
            height: calc(100vh - 60px);
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .message-box {
            padding: 15px;
            overflow-y: auto;
            flex-grow: 1;
            display: flex;
            flex-direction: column;
            justify-content: flex-start;
        }


        .message {
            margin-bottom: 10px;
            max-width: 80%;
            padding: 10px 15px;
            border-radius: 15px;
            word-wrap: break-word;
        }

        .sent {
            background-color: #d1e7dd;
            align-self: flex-end;
        }

        .received {
            background-color: #f0f0f0;
            align-self: flex-start;
        }

        .chat-form {
            padding: 10px;
            border-top: 1px solid #ddd;
            background-color: white;
        }

        textarea {
            resize: none;
        }
    </style>
</head>
<body>

<div class="d-flex justify-content-between align-items-center p-2 bg-primary text-white">
    <span><i class="fa-solid fa-arrow-left me-2"></i> <a href="javascript:history.back()" class="text-white text-decoration-none">Back</a></span>
    <span><i class="fa-solid fa-comments me-2"></i> Chat with ${receiver.name}</span>
</div>

<div class="chat-container">
    <div class="message-box">
        <c:forEach var="msg" items="${messages}">
            <div class="message ${msg.sender.id == user.id ? 'sent ms-auto' : 'received'}">
                <strong>${msg.sender.name}:</strong><br />
                    ${msg.messageContent}
            </div>
        </c:forEach>
    </div>

    <div class="chat-form">
        <form action="${pageContext.request.contextPath}/messages/application/${application.id}/send" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <div class="input-group">
                <textarea name="content" class="form-control" rows="2" placeholder="Type a message..." required></textarea>
                <button type="submit" class="btn btn-primary"><i class="fa-solid fa-paper-plane"></i></button>
            </div>
        </form>
    </div>
</div>

<script>
    if (window.history.replaceState) {
        window.history.replaceState(null, null, window.location.href);
    }
</script>

</body>
</html>
