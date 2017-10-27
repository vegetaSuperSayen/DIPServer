<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Grayscale - File Upload to Database</title>
<link rel="stylesheet" type="text/css" href="../css/materialize.min.css"
	media="screen">
<script src="../js/materialize.min.js"></script>
</head>
<body>
	<main>
	<div class="container">
		<h3>File Upload to Database</h3>
		<form method="post" action=fileupload enctype="multipart/form-data">
			<table border="0">
				<!-- 				<tr>
					<td>First Name:</td>
					<td><input type="text" name="firstName" size="50" /></td>
				</tr>
				<tr>
					<td>Last Name:</td>
					<td><input type="text" name="lastName" size="50" /></td>
				</tr>
 -->
				<tr>
					<td>Email:</td>
					<td><input type="text" name="email" size="50" /></td>
				</tr>
				<tr>
					<td>Phone:</td>
					<td><input type="text" name="phone" size="50" /></td>
				</tr>
				<tr>
					<td>Photo:</td>
					<td><input type="file" name="photo" size="50" /></td>
					
				</tr>
				<tr>
					<td colspan="2"><input class="btn btn-success" type="submit"
						value="Convert"></td>
				</tr>
			</table>
		</form>
		
		<div class="container">
	</main>

</body>
</html>
