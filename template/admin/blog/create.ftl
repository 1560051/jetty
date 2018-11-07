<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create New Article</title>
</head>
<body>
<h3>Create new Article</h3>
<form action="./create" method="post">
    <div>
        <label for="title">Title:</label>
        <input type="text" name="title" id="title">
    </div><br/>
    <div>
        <label for="tag">Tags:</label>
        <input type="text" id="tag" name="tag">
    </div><br/>
    <div>
        <label for="content">Content:</label>
        <textarea id="content" name="content"></textarea>
        <input type="submit" value="Create article">
    </div>
</form>
</body>
</html>