<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lieberry - Search</title>
    </head>
    <body>
        <jsp:include page="../jspf/header.jspf" />
        <form action="search" method="POST">
            <fieldset>
                <legend>Search</legend>
                <input type="text" name="query" />
                <br/>
                <input type="radio" name="field" value="title" checked="true"/> Title
                <input type="radio" name="field" value="author" /> Author
                <input type="radio" name="field" value="isbn" /> ISBN
                <input type="radio" name="field" value="publisher" /> Publisher
                <input type="radio" name="field" value="publicationYear" /> Publication year
                <br />
                <input type="submit" value="Search" />
            </fieldset>
        </form>
        
        <c:if test="${not empty books}">
            <h2>Results</h2>
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
                        <td><form:form action="${pageContext.request.contextPath}/app/books/${book.isbn}" method="DELETE"><input type="submit" value="delete"></form:form>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        
        <c:if test="${empty books && didSearch}">
            No results :(
        </c:if>
        <jsp:include page="../jspf/footer.jspf" />
    </body>
</html>
