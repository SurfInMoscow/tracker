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

<c:set var="backlog" value="${backlog}"/>
<jsp:useBean id="backlog" type="ru.vorobyev.tracker.domain.project.Backlog"/>

<c:set var="project" value="${project}"/>
<jsp:useBean id="project" type="ru.vorobyev.tracker.domain.project.Project"/>

<c:set var="user" value="${user}"/>
<jsp:useBean id="user" type="ru.vorobyev.tracker.domain.user.User"/>

<div class="container">
    <div class="pt-2  pb-1 jumbotron" style="background-color: #f5f5f5">
        <form action="issues" method="post">
            <input type="hidden" name="backlog_id" value="${backlog.id}">
            <input type="hidden" name="project_id" value="${project.id}">
            <div class="form-group">
                <label for="issueName">Имя:</label>
                <input type="text" class="form-control" id="issueName" name="name">
            </div>
            <br>
            <div class="form-group">
                <label for="priority">Тип:</label>
                <select class="form-control" id="type" name="type">
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
                    <option>LOW</option>
                    <option>MEDIUM</option>
                    <option>HIGH</option>
                </select>
            </div>
            <br>
            <div class="form-group">
                <label for="status">Статус:</label>
                <select class="form-control" id="status" name="status">
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
                <input type="email" class="form-control" id="reporter" name="reporter" value="${user.email}">
            </div>
            <br>
            <div class="form-group">
                <label for="executor">Исполнитель:</label>
                <input type="email" class="form-control" id="executor" name="executor" placeholder="name@example.com">
            </div>
            <br>
            <button type="button" onclick="window.history.back()" class="btn btn-secondary">Отмена</button>
            <button type="submit" class="btn btn-success" name="old_type" value="mock">Сохранить</button>
        </form>
    </div>
</div>

</body>
</html>