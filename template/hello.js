$(document).ready(function() {
    $.ajax({
        url: "localhost:8080/apis/blog/"
    }).then(function(data) {
        console.log(data)
    });
});
