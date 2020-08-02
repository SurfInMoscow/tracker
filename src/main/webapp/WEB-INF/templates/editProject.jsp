<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://pavel.vorobev.com" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <c:set var="project" value="${project}"/>
    <jsp:useBean id="project" type="ru.vorobyev.tracker.domain.project.Project"/>
    <title>Проект <c:out value="${project.name}"/></title>
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

    div.projectForm {
        left: 3%;
        right: 3%;
    }

    thead {
        display: block;
    }

    tbody {
        display: block;
        overflow-y: scroll;
        height: 500px;
    }

    td, th {
        width: 200px;
    }

    .table-bordered.table {
        width: 1000px !important;
    }

    .list-group {
        position: absolute;
        width: 200px;
        top: 370px;
    }

</style>
<body>
<jsp:include page="fragments/navbar.jsp"/>

<div class="pl-3 projectForm">
    <form action="projects" method="post" name="projectForm">
        <div class="form-row">
            <input type="hidden" name="id" value="${project.id}">
            <div class="form-group col-md-1">
                <label for="name">Имя</label>
                <input type="text" class="form-control" id="name" name="name" value="${project.name}">
            </div>
            <div class="form-group col-md-3">
                <label for="description">Описание</label>
                <textarea class="form-control" id="description" name="description"
                          rows="2">${project.description}</textarea>
            </div>
            <div class="form-group col-md-2">
                <label for="department">Департамент</label>
                <input type="text" class="form-control" id="department" name="department" value="${project.department}">
            </div>
            <div class="form-group col-md-2">
                <label for="manager_email">Email менеджера</label>
                <input type="email" class="form-control" id="manager_email" name="manager_email"
                       aria-describedby="emailHelp" value="${project.manager}">
            </div>
            <div class="form-group col-md-2">
                <label for="admin_email">Email администратора</label>
                <input type="email" class="form-control" id="admin_email" name="admin_email"
                       aria-describedby="emailHelp" value="${project.administrator}">
            </div>
        </div>
        <c:set var="user" value="${user}"/>
        <jsp:useBean id="user" type="ru.vorobyev.tracker.domain.user.User"/>
        <c:if test="${project.administrator eq user.email or project.manager eq user.email}">
            <button type="submit" class="btn btn-success">Сохранить</button>
        </c:if>
    </form>

</div>

<br>

<div class="fixed-top">
    <c:choose>
        <c:when test="${project.administrator eq user.email or project.manager eq user.email}">
            <button type="button" class="btn btn-info" data-toggle="modal" data-target="#userInviteModal"
                    style="position: absolute; left: 17px; top: 325px;">Пригласить
            </button>
        </c:when>
        <c:when test="${project.administrator ne user.email and project.manager ne user.email}">
            <button type="button" class="btn btn-info" data-toggle="modal" data-target="#userInviteModal"
                    style="position: absolute; left: 17px; top: 325px;" disabled>Пригласить
            </button>
        </c:when>
    </c:choose>
    <a href="issues?action=create&project_id=${project.id}">
        <button type="submit" class="btn btn-info" style="position: absolute; left: 17px; top: 280px;">Задача</button>
    </a>
</div>

<br>

<div class="fixed-top">
    <div id="list-example" class="list-group">
        <a class="list-group-item list-group-item-action" href="#bugs">Баги</a>
        <a class="list-group-item list-group-item-action" href="#epics">Эпики</a>
        <a class="list-group-item list-group-item-action" href="#stories">Истории</a>
        <a class="list-group-item list-group-item-action" href="#tasks">Таски</a>
        <a class="list-group-item list-group-item-action" href="#sprint">Спринт</a>
        <a class="list-group-item list-group-item-action" data-toggle="modal" data-target="#participants">Участники</a>
    </div>
</div>

<div class="container">
    <div class="pt-2  pb-1 jumbotron" id="bugs" style="background-color: #f5f5f5">
        <h5 class="text-left">Баги:</h5>
        <table class="table table-bordered">
            <thead class="thead" style="background-color: #67bfde">
            <tr>
                <th>Дата:</th>
                <th>Имя:</th>
                <th>Приоритет:</th>
                <th>Статус:</th>
                <th>Действия:</th>
            </tr>
            </thead>
            <tbody class="text-left">
            <c:forEach var="bugs" items="${bugs}">
                <jsp:useBean id="bugs" scope="page" type="ru.vorobyev.tracker.domain.issue.Bug"/>
                <tr>
                    <td>${f:formatLocalDateTime(bugs.creationDate, "yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${bugs.name}</td>
                    <td>${bugs.priority}</td>
                    <td>${bugs.status}</td>
                    <td>
                        <a href="issues?action=edit&project_id=${project.id}&type=Bug&id=${bugs.id}">
                            <button type="submit" class="btn btn-info btn-sm">Детали</button>
                        </a>
                        <a href="issues?action=delete&project_id=${project.id}&type=Bug&id=${bugs.id}">
                            <button type="submit" class="btn btn-warning btn-sm">Удалить</button>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="pt-2  pb-1 jumbotron" id="epics" style="background-color: #f5f5f5">
        <h5 class="text-left">Эпики:</h5>
        <table class="table table-bordered">
            <thead class="thead" style="background-color: #67bfde">
            <tr>
                <th>Дата:</th>
                <th>Имя:</th>
                <th>Приоритет:</th>
                <th>Статус:</th>
                <th>Действия:</th>
            </tr>
            </thead>
            <tbody class="text-left">
            <c:forEach var="epics" items="${epics}">
                <jsp:useBean id="epics" scope="page" type="ru.vorobyev.tracker.domain.issue.Epic"/>
                <tr>
                    <td>${f:formatLocalDateTime(epics.creationDate, "yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${epics.name}</td>
                    <td>${epics.priority}</td>
                    <td>${epics.status}</td>
                    <td>
                        <a href="issues?action=edit&project_id=${project.id}&type=Epic&id=${epics.id}">
                            <button type="submit" class="btn btn-info btn-sm">Детали</button>
                        </a>
                        <a href="issues?action=delete&project_id=${project.id}&type=Epic&id=${epics.id}">
                            <button type="submit" class="btn btn-warning btn-sm">Удалить</button>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="pt-2  pb-1 jumbotron" id="stories" style="background-color: #f5f5f5">
        <h5 class="text-left">Истории:</h5>
        <table class="table table-bordered">
            <thead class="thead" style="background-color: #67bfde">
            <tr>
                <th>Дата:</th>
                <th>Имя:</th>
                <th>Приоритет:</th>
                <th>Статус:</th>
                <th>Действия:</th>
            </tr>
            </thead>
            <tbody class="text-left">
            <c:forEach var="stories" items="${stories}">
                <jsp:useBean id="stories" scope="page" type="ru.vorobyev.tracker.domain.issue.Story"/>
                <tr>
                    <td>${f:formatLocalDateTime(stories.creationDate, "yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${stories.name}</td>
                    <td>${stories.priority}</td>
                    <td>${stories.status}</td>
                    <td>
                        <a href="issues?action=edit&project_id=${project.id}&type=Story&id=${stories.id}">
                            <button type="submit" class="btn btn-info btn-sm">Детали</button>
                        </a>
                        <a href="issues?action=delete&project_id=${project.id}&type=Story&id=${stories.id}">
                            <button type="submit" class="btn btn-warning btn-sm">Удалить</button>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="pt-2  pb-1 jumbotron" id="tasks" style="background-color: #f5f5f5">
        <h5 class="text-left">Таски:</h5>
        <table class="table table-bordered">
            <thead class="thead" style="background-color: #67bfde">
            <tr>
                <th>Дата:</th>
                <th>Имя:</th>
                <th>Приоритет:</th>
                <th>Статус:</th>
                <th>Действия:</th>
            </tr>
            </thead>
            <tbody class="text-left">
            <c:forEach var="tasks" items="${tasks}">
                <jsp:useBean id="tasks" scope="page" type="ru.vorobyev.tracker.domain.issue.Task"/>
                <tr>
                    <td>${f:formatLocalDateTime(tasks.creationDate, "yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${tasks.name}</td>
                    <td>${tasks.priority}</td>
                    <td>${tasks.status}</td>
                    <td>
                        <a href="issues?action=edit&project_id=${project.id}&type=Task&id=${tasks.id}">
                            <button type="submit" class="btn btn-info btn-sm">Детали</button>
                        </a>
                        <a href="issues?action=delete&project_id=${project.id}&type=Task&id=${tasks.id}">
                            <button type="submit" class="btn btn-warning btn-sm">Удалить</button>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="pt-2  pb-1 jumbotron" id="sprint" style="background-color: #f5f5f5">
        <h5 class="text-left">Спринт:</h5>
        <table class="table table-bordered">
            <thead class="thead" style="background-color: #67bfde">
            <tr>
                <th>Дата:</th>
                <th>Имя:</th>
                <th>Приоритет:</th>
                <th>Статус:</th>
                <th>Действия:</th>
            </tr>
            </thead>
            <tbody class="text-left">
            <c:forEach var="sprints" items="${sprints}">
                <jsp:useBean id="sprints" scope="page" type="ru.vorobyev.tracker.domain.issue.AbstractIssue"/>
                <tr>
                    <td>${f:formatLocalDateTime(sprints.creationDate, "yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${sprints.name}</td>
                    <td>${sprints.priority}</td>
                    <td>${sprints.status}</td>
                    <td>
                        <a href="issues?action=edit&project_id=${project.id}&type=Abstract&id=${sprints.id}">
                            <button type="submit" class="btn btn-info btn-sm">Детали</button>
                        </a>
                        <a href="issues?action=delete&project_id=${project.id}&type=Abstract&id=${sprints.id}">
                            <button type="submit" class="btn btn-warning btn-sm">Удалить</button>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel"
     aria-hidden="true" id="participants">
    <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h5 class="modal-title">Участники</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <!-- Modal body -->
            <div class="modal-body">
                <table class="table-bordered" style="width: auto">
                    <thead style="background-color: #67bfde; display: block">
                    <tr>
                        <th>Имя:</th>
                        <th>Email:</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody style="height: auto">
                    <c:forEach var="users" items="${users}">
                        <jsp:useBean id="users" scope="page" type="ru.vorobyev.tracker.domain.user.User"/>
                        <tr>
                            <td>${users.name}</td>
                            <td>${users.email}</td>
                            <c:choose>
                                <c:when test="${project.administrator eq user.email or project.manager eq user.email}">
                                    <td>
                                        <form action="projects" method="get">
                                            <input type="hidden" name="id" value="${project.id}">
                                            <input type="hidden" name="user_email" value="${users.email}">
                                            <input type="hidden" name="action" value="dropuser">
                                            <button type="submit" class="btn btn-warning btn-sm" data-toggle="modal">
                                                Удалить
                                            </button>
                                        </form>
                                    </td>
                                </c:when>
                                <c:when test="${project.administrator ne user.email and project.manager ne user.email}">
                                    <td>
                                        <button type="button" class="btn btn-warning btn-sm" data-toggle="modal"
                                                disabled>
                                            Удалить
                                        </button>
                                    </td>
                                </c:when>
                            </c:choose>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<div class="modal" id="userInviteModal">
    <div class="modal-dialog modal-sm" style="position: absolute; top: 300px; left: 140px">
        <div class="modal-content">
                <!-- Modal Header -->
                <div class="modal-header">
                    <p class="modal-title">Добавить пользователя</p>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
                    <form action="projects" method="post">
                        <div class="form-group">
                            <label for="userEmail">Email</label>
                            <input type="hidden" name="id" value="${project.id}">
                            <input type="email" name="user_email" class="form-control" id="userEmail"
                                   aria-describedby="emailHelp">
                        </div>
                        <br>
                        <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Закрыть</button>
                        <button type="submit" class="btn btn-danger btn-sm">Сохранить</button>
                    </form>
                </div>
        </div>
    </div>
</div>

</body>
</html>