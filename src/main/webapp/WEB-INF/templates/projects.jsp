<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session = "false" %>
<!DOCTYPE html>
<head>
    <title>Projects</title>
</head>
<jsp:include page="fragments/head.jsp"/>
<style>
    body {
        background-color: #f5f5f5;
    }
</style>
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
                    <input type="hidden" name="_csrf" value="${{_csrf.token}}"/>
                </form>
            </li>
        </ul>
        <span class="navbar-text">Bug Tracker</span>
    </div>
</nav>
<div class="container">
    <div class="jumbotron" style="background-color: #f5f5f5">
        <input class="form-control" id="myInput" type="text" placeholder="Search..">
        <br>
        <div class="container">
            <h3 class="text-left">Мои проекты:
                <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#myModal">
                    Создать
                </button>
            </h3>
            <table class="table table-bordered">
                <thead class="thead" style="background-color: #67bfde">
                <tr>
                    <th scope="col">Имя</th>
                    <th scope="col">Описание</th>
                    <th scope="col">Департамент</th>
                    <th scope="col"></th>
                </tr>
                </thead>
                <tbody id="myTable">
                <c:set var="user" scope="page" value="${user}"/>
                <jsp:useBean id="user" scope="page" type="ru.vorobyev.tracker.domain.user.User"/>
                <c:forEach var="projects" items="${projects}">
                    <jsp:useBean id="projects" scope="page" type="ru.vorobyev.tracker.domain.project.Project"/>
                    <tr>
                        <td>${projects.name}</td>
                        <td>${projects.description}</td>
                        <td>${projects.department}</td>
                        <td>
                            <a href="projects?action=edit&id=${projects.id}">
                                <button type="submit" class="btn btn-info btn-sm">Детали</button>
                            </a>
                            <c:if test="${projects.administrator eq user.email}">
                                <a href="projects?action=delete&id=${projects.id}">
                                    <button type="button" class="btn btn-danger btn-sm">Удалить</button>
                                </a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- The Modal -->
<div class="modal" id="myModal">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h3 class="modal-title">Создание проекта</h3>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
                <form action="projects" method="post">
                    <div class="form-group">
                        <label for="name" class="col-form-label">Имя проекта</label>
                        <input type="text" class="form-control" id="name" name="name"/>
                    </div>
                    <div class="form-group">
                        <label for="description" class="col-form-label">Описание</label>
                        <input type="text" class="form-control" id="description" name="description"/>
                    </div>
                    <div class="form-group">
                        <label for="department" class="col-form-label">Департамент</label>
                        <input type="text" class="form-control" id="department" name="department"/>
                    </div>
                    <div class="form-group">
                        <label for="manager_email" class="col-form-label">Email менеджера</label>
                        <input type="text" class="form-control" id="manager_email" name="manager_email"/>
                    </div>
                    <div class="form-group">
                        <label for="admin_email" class="col-form-label">Email администратора</label>
                        <input type="text" class="form-control" id="admin_email" name="admin_email"/>
                    </div>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Отмена</button>
                    <input type="hidden" name="_csrf" value="${{_csrf.token}}"/>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $("#myInput").on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $("#myTable tr").filter(function () {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
</script>
</body>
</html>