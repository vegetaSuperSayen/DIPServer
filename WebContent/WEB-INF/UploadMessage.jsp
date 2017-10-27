<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>File Upload to Database</title>
<link rel="stylesheet" type="text/css" href="../css/materialize.min.css"
	media="screen">
<script src="../js/materialize.min.js"></script>
</head>
<body>
	<main>
	<div class="container center">
		<h3><%=request.getAttribute("Message")%></h3>
		<img src="<%=request.getAttribute("url2")%>" />
		<p>
			<a class="btn btn-success" href="<%=request.getAttribute("url2")%>">Do not click here!</a>
			<a class="btn btn-success" href="uploadform">Convert another image?</a>
		</p>
		<!-- link of the new s3 bucket item in grayscale uploaded -->
	</div>
	</main>
</body>
</html>