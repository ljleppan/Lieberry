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
            <c:forEach var="author" items="${book.authors}">
                <a href="${pageContext.request.contextPath}/app/books/author/${author}"><c:out value="${author}"/>; </a>
            </c:forEach>
            <br/>
            ISBN:
            <c:out value="${book.isbn}" />
            <br />
            Publisher: 
            <c:out value="${book.publisher}" />
            <br />
            Publication year: 
            <c:out value="${book.publicationYear}" />
            <br /> 
        </div>
<%@include file="/WEB-INF/jspf/footer.jspf"  %>
