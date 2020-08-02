<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Задача</title>
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

<c:set var="id" value="${id}"/>

<c:set var="name" value="${name}"/>

<c:set var="type" value="${type}"/>

<c:set var="old_type" value="${type}"/>

<c:set var="priority" value="${priority}"/>

<c:set var="status" value="${status}"/>

<c:set var="reporter" value="${reporter}"/>

<c:set var="executor" value="${executor}"/>

<c:set var="backlog" value="${backlog}"/>
<jsp:useBean id="backlog" type="ru.vorobyev.tracker.domain.project.Backlog"/>

<c:set var="project" value="${project}"/>
<jsp:useBean id="project" type="ru.vorobyev.tracker.domain.project.Project"/>

<div class="container">
    <div class="pt-2  pb-1 jumbotron" style="background-color: #f5f5f5">
        <form action="issues" method="post">
            <input type="hidden" name="backlog_id" value="${backlog.id}">
            <input type="hidden" name="project_id" value="${project.id}">
            <input type="hidden" name="id" value="${id}">
            <input type="hidden" name="old_type" value="${old_type}">
            <div class="form-group">
                <label for="issueName">Имя:</label>
                <input type="text" class="form-control" id="issueName" name="name" value="${name}">
            </div>
            <br>
            <div class="form-group">
                <label for="priority">Тип:</label>
                <select class="form-control" id="type" name="type">
                    <option selected>${type}</option>
                    <option>Bug</option>
                    <option>Epic</option>
                    <option>Story</option>
                    <option>Task</option>
                </select>
            </div>
            <br>
            <div class="form-group">
                <label for="priority">Приоритет:</label>
                <select class="form-control" id="priority" name="priority">
                    <option selected>${priority}</option>
                    <option>LOW</option>
                    <option>MEDIUM</option>
                    <option>HIGH</option>
                </select>
            </div>
            <br>
            <div class="form-group">
                <label for="status">Статус:</label>
                <select class="form-control" id="status" name="status">
                    <option selected>${status}</option>
                    <option>OPEN_ISSUE</option>
                    <option>IN_PROGRESS_ISSUE</option>
                    <option>REVIEW_ISSUE</option>
                    <option>TEST_ISSUE</option>
                    <option>RESOLVED_ISSUE</option>
                    <option>RE_OPENED_ISSUE</option>
                    <option>CLOSE_ISSUE</option>
                </select>
            </div>
            <br>
            <div class="form-group">
                <label for="reporter">Репортер:</label>
                <input type="email" class="form-control" id="reporter" name="reporter" value="${reporter}">
            </div>
            <br>
            <div class="form-group">
                <label for="executor">Исполнитель:</label>
                <input type="email" class="form-control" id="executor" name="executor" value="${executor}">
            </div>
            <br>
            <button type="button" onclick="window.history.back()" class="btn btn-secondary">Отмена</button>
            <button type="submit" class="btn btn-success">Сохранить</button>
            <button type="submit" class="btn btn-info" name="toSprint" value="true">В спринт</button>
            <input type="hidden" name="_csrf" value="${{_csrf.token}}"/>
        </form>
    </div>
</div>

</body>
</html>