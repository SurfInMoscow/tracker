<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

<br>
