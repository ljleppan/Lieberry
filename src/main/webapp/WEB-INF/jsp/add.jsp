<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lieberry - Add a book" /></title>
    </head>
    <body>
        <h1>Add a book</h1>
        <form:form commandName="book" action="${pageContext.request.contextPath}/app/books" method="POST">
            <form:input path="title" /><form:errors path="title"/><br />
            <form:input path="author" /><form:errors path="author"/><br />
            <form:input path="isbn" /><form:errors path="isbn"/><br />
            <form:input path="publisher" /><form:errors path="publisher"/><br />
            <form:input path="publicationYear" /><form:errors path="publicationYear"/><br />
            <input type="submit" />
        </form:form>
    </body>
</html>