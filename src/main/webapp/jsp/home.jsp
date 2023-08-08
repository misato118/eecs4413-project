<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html >

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="css/grocerystore.css" type="text/css" />
<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
<script src="js/grocerystore.js"></script>
</head>
<body>
	<div id="centered">
	
		<jsp:include page="header.jsp" flush="true" />
		<br />
		<jsp:include page="leftColumn.jsp" flush="true" />
		<span class="label" style="margin-left: 15px;">Featured Groceries</span>

		<a class="link" href="${initParam.param2}?action=identification"><span>SignIn/SignUp</span></a>
		<a class="link" href="${initParam.param2}?action=logout"><span>Logout</span></a>
		
		<!-- TODO: Change the images here? -->
		
		<table>
			<tr>
				<td><span class="tooltip_img1"><img src="${initParam.imageURL}/cucumber.png"/>Cucumber</span></td>
				<td><img src="${initParam.imageURL}/green onion.png" /></td>
				<td><img src="${initParam.imageURL}/lemon.png" /></td>
				<!--  <td><img src="${initParam.imageURL}/A9781430231684-small_8.png" /></td>-->
				<!--  <td><img src="${initParam.imageURL}/A9781430249474-small_1.png" /></td>-->
			</tr>
			<tr>
				<td><img src="${initParam.imageURL}/watermelon.png" /></td>
				<td><img src="${initParam.imageURL}/ground beef.png" /></td>
				<td><img src="${initParam.imageURL}/chicken breast.png" /></td>
				<!-- <td><img src="${initParam.imageURL}/A9781430246596-small_1.png" /></td>-->
				<!-- <td><img src="${initParam.imageURL}/A9781430257349-small_1.png" /></td>-->
			</tr>
		</table>
		
		
	</div>
</body>
</html>