<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle}" default="Online Grocery Store" /> - Aura Grocery</title>
    <!-- Modern Google Fonts: Plus Jakarta Sans & Outfit -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;600;700;800&family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <!-- Modern Multi-Tier Navigation Bar -->
    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <!-- Main Content Container -->
    <main class="container">

        <!-- Flash Alert Messages (Post-Redirect-Get / Query Params) -->
        <c:if test="${not empty flashMessage}">
            <div class="alert alert-${flashType eq 'error' ? 'danger' : 'success'}">
                <c:out value="${flashMessage}" />
            </div>
        </c:if>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success">
                <c:out value="${param.msg}" />
            </div>
        </c:if>

        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">
                <c:out value="${param.error}" />
            </div>
        </c:if>
