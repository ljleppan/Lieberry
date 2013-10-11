<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lieberry - All books</title>
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
                        <td></td>
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
                        <td><form:form action="${pageContext.request.contextPath}/app/books/${book.isbn}" method="DELETE"><input type="submit" value="delete"></form:form>
                    </tr>
                </c:forEach>
            </table>
        </c:if>    
    </body>
</html>
