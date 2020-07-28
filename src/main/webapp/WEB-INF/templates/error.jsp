<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error page</title>
</head>
<style>
    body {
        background-color: #f5f5f5;
    }
</style>
<jsp:include page="fragments/head.jsp"/>
<body>
<nav class="navbar navbar-expand-lg" style="background-color: #f5f5f5">
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <form action="projects" method="get">
                    <button type="submit" class="btn btn-light">Проекты</button>
                </form>
            </li>
            <li class="nav-item active">
                <form action="projects" method="get">
                    <button type="submit" name="action" value="logout" class="btn btn-light">Выход</button>
                </form>
            </li>
        </ul>
        <span class="navbar-text">Bug Tracker</span>
    </div>
</nav>
<c:set var="exception" scope="page" value="${exception}"/>
<jsp:useBean id="exception" scope="page" type="ru.vorobyev.tracker.web.ExceptionBean"/>
<h1>Ошибка!</h1>
<br>
<p><c:out value="${exception.message}"/></p>
</body>
</html>