<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - <c:out value="${book.title}" /></title>

<%@include file="/WEB-INF/jspf/header.jspf"  %>

        <div class="jumbotron ">
            <h1><c:out value="${book.title}" /></h1>
            <img src="https://covers.openlibrary.org/b/isbn/<c:out value="${book.isbn}"/>-M.jpg" alt="Cover for <c:out value="{$book.name}"/>." />
        </div>
        
        <div class="">
            Title: 
            <c:out value="${book.title}" />
            <br />
            Authors: 
            <a href="${pageContext.request.contextPath}/app/books/author/${book.authors[0]}"><c:out value="${book.authors[0]}"/></a><c:if test="${fn:length(book.authors) > 1}"><c:forEach var="index" begin="1" end="${fn:length(book.authors) - 1}">, <a href="${pageContext.request.contextPath}/app/books/author/${book.authors[index]}"><c:out value="${book.authors[index]}"/></a></c:forEach></c:if>
            <br/>
            ISBN:
            <c:out value="${book.isbn}" />
            <br />
            Publisher: 
            <a href="${pageContext.request.contextPath}/app/books/publisher/${book.publisher}"><c:out value="${book.publisher}" /></a>
            <br />
            Publication year: 
            <a href="${pageContext.request.contextPath}/app/books/publicationyear/${book.publicationYear}"><c:out value="${book.publicationYear}" /></a>
            <br /> 
        </div>
<%@include file="/WEB-INF/jspf/footer.jspf"  %>