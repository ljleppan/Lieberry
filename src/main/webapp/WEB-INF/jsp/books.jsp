<%-- 
    Document   : books
    Created on : 11-Oct-2013, 13:15:29
    Author     : ljleppan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lieberry</title>
    </head>
    <body>
        <h1>All our books:</h1>
        <c:if test="${not empty books}">
            <table>
                <thead>
                    <tr>
                        <td>Cover</td>
                        <td>Title</td>
                        <td>Author</td>
                        <td>Publisher</td>
                        <td>Publication year</td>
                        <td>ISBN</td>
                    </tr>
                </thead>
                <c:forEach var="book" items="${books}">
                    <tr>
                        <td>
                            <img src="https://covers.openlibrary.org/b/isbn/<c:out value='${book.isbn}'/>-S.jpg" alt="Cover for <c:out value='${book.title}'/>." />
                        </td>
                        <td><c:out value="${book.title}"/></td>
                        <td><c:out value="${book.author}"/></td>
                        <td><c:out value="${book.publisher}"/></td>
                        <td><c:out value="${book.publicationYear}"/></td>
                        <td><c:out value="${book.isbn}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>    
    </body>
</html>
