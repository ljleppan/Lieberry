<%-- 
    Document   : book
    Created on : 11-Oct-2013, 13:15:21
    Author     : ljleppan 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${book.title}" /></title>
    </head>
    <body>
        <h1><c:out value="${book.title}" /></h1>
        <img src="https://covers.openlibrary.org/b/isbn/<c:out value="${book.isbn}"/>-M.jpg" alt="Cover for <c:out value="{$book.name}"/>." /><br />
        Title: <c:out value="${book.title}" /><br />
        Author: <c:out value="${book.author}" /><br />
        ISBN: <c:out value="${book.isbn}" /><br />
        Publisher: <c:out value="${book.publisher}" /><br />
        Publication year: <c:out value="${book.publicationYear}" /><br /> 
    </body>
</html>
