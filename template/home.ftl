<html>
<head>
    <title>Home</title>
        </head>
<body>


<p>List Article</p>

<ul>
    <#list list as article>
    <a href="http://localhost:8080/home/article?id=${article.getId()}">${article.getTitle()}</a><br/>
</#list>
</ul>

</body>
</html>