<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login</title>
</head>
<jsp:include page="fragments/head.jsp"/>
<style>
    body {
        background-color: #f5f5f5;
    }
</style>
<style>
    form {
        position: fixed;
        top: 30%;
        left: 40%;
        width: 250px;
        border-radius: 5px;
        background-color: #f2f2f2;
        padding: 15px;
    }
</style>
<body>
<form method="post" action="tracker">
    <div class="form-group">
        <label for="exampleInputEmail1">Email</label>
        <input type="email" name="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp"
               size="15">
    </div>
    <div class="form-group mx-auto">
        <label for="exampleInputPassword1">Password</label>
        <input type="password" name="password" class="form-control" id="exampleInputPassword1" size="15">
    </div>
    <button type="submit" class="btn btn-primary">Вход</button>
</form>
</body>
</html>