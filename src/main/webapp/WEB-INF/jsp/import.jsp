<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - Import a book</title>

<%@include file="/WEB-INF/jspf/header.jspf"  %>

        <div class="hero-unit text-center ">
            <h1>Import a book</h1>
            <p class="lead">Import book(s) from Open Library.</p>

            <div>
                <form action="${pageContext.request.contextPath}/app/import" method="POST" class="form-horizontal">
                    <div class="controls-group">
                        <div class="controls-row">
                            <input type="text" name="query" class="search-query input-xlarge" />
                        </div>

                        <div class="controls-row">                   
                            <label for="title" class="radio inline">Title</label>
                            <input type="radio" name="field" id="title" value="title" checked="true"/> 

                            <label for="author" class="radio inline">Author</label>
                            <input type="radio" name="field" id="author" value="author" />

                            <label for="isbn" class="radio inline">ISBN</label>
                            <input type="radio" name="field" id="isbn" value="isbn" />
                        </div>
                    </div>

                    <div class="controls-row">
                        <br />
                        <input value="Search" type="submit" class="btn btn-large btn-primary"/>
                    </div>
                </form>
            </div>
        </div>
<%@include file="/WEB-INF/jspf/footer.jspf"  %>
