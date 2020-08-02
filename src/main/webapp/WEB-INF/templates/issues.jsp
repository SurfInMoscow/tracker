<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <c:set var="issue" value="${issue}"/>
    <jsp:useBean id="issue" type="ru.vorobyev.tracker.domain.issue.AbstractIssue"/>
    <title><c:out value="${issue.name}"/></title>
</head>
<jsp:include page="fragments/head.jsp"/>
<style>
    body {
        background-color: #f5f5f5;
        height: auto;
        width: 100%;
        margin: 0;
        padding: 0;
        overflow: auto;
    }
</style>
<body>
<jsp:include page="fragments/navbar.jsp"/>

issues
</body>
</html>