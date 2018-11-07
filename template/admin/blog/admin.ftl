<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Administrator Page</title>
</head>
<body>
<h1>Hello admin</h1><br/>

<br/>
<br/>
<h3>Create a article</h3>

<form action="/admin/blog/create-form" method="post">
<input type="submit" value="Create">
</form>

<br/>
<br/>

<h3>Article Management</h3>
<button type="submit" onclick="window.location='/admin/blog/list';" />Edit Article</button>
</body>
</html>