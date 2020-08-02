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
<jsp:include page="fragments/navbar.jsp"/>
<c:set var="exception" scope="page" value="${exception}"/>
<jsp:useBean id="exception" scope="page" type="ru.vorobyev.tracker.web.ExceptionBean"/>
<h3>Ошибка!</h3>
<br>
<p><c:out value="${exception.message}"/></p>
</body>
</html>