<%--
  Created by IntelliJ IDEA.
  User: Andrey
  Date: 6/1/2017
  Time: 13:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<html>
<head>
    <title>Title</title>
</head>
<body>


<input id="addFirmwareFile" type="file" class="form-control" name="file"
       style="width: 500px" multiple>

<button type="button"
        onclick="uploadFirmware(this)">Upload
</button>


<div>
<form action="/show" method="post">

    <p><input type="submit"></p>
</form>
    </div>


</body>

<script>

    function uploadFirmware(el) {

        var formData = new FormData();
        var totalFiles = document.getElementById("addFirmwareFile").files.length;
        for (var i = 0; i < totalFiles; i++)
        {
            var file = document.getElementById("addFirmwareFile").files[i];
            formData.append("file", file);
        }
        $.ajax({
            url: '/upload',
            type: 'POST',
            data: formData,
            enctype: 'multipart/form-data',
            cache: false,
            contentType: false,
            processData: false,
        })
    }

    function createFolder() {
        var nameFodler = prompt("Please enter folder name");

        if(nameFodler != ""){
            $.ajax({
                url: "/createFolder",
                method: "POST",
                dataType: 'text',
                cache: false,
                data:{nameFodler:nameFodler},
                success: function (data) {

                },
                error: function (data) {
                    alert("Error!!!");

                }
            })
        }
    }




</script>
</html>
