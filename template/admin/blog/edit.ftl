<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Form</title>
</head>
<body>
    <h2>Edit ID: ${article.getId()}</h2>
    <form action="/admin/blog/edit/${article.getId()}" method="post">
     <input type="number" id="idEdited" name="idEdited" value="${article.getId()}" hidden><br/><br/>
        Title: <input type="text" name="titleEdit" id="titleEdit" value="${article.getTitle()}"><br/><br/>
        Tag: <input type="text" id="tagEdit" name="tagEdit" value="${article.getTag()}"><br/><br/>
        Content: <textarea id="contentEdit" name="contentEdit">${article.getContent()}</textarea>
        <input type="submit" value="Edit">
        </form>


<button onclick="document.getElementById('id01').style.display='block';" getid(4,json="" introduce,json,json="" example="" v);="">Edit</button>
</body>
</html>