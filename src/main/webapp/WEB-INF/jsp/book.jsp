<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lieberry - <c:out value="${book.title}" /></title>
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
