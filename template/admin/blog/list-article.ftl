<html>
<head>
    <title>Home</title>
        </head>
<body>


<p>List Article</p>

<ul>
    <#list list as article>
      <h3 name="idArticle"}>${article.getId()}</h3>
      Title: <input type="text" name="title" value="${article.getTitle()}" disabled>
      <button type="submit" onclick="window.location='./edit/${article.getId()}';">Edit</button>
      <button type="submit" onclick="window.location='./delete/${article.getId()}';">Delete</button><br/>
      <hr>
</#list>
</ul>

</body>
</html>