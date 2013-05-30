<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head> 
<title>Computers database error page</title>
<link rel="stylesheet" type="text/css" media="screen"
	href="style/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="style/main.css">
</head>
<body>

	<header class="topbar">
		<h1 class="fill">
			<a href="affichageComputers"> My super Computer database </a>
		</h1>
	</header>

	<section id="main">


		<h1>Error Page</h1>


		<c:if test="${! empty error }">
			 <div class="alert-message warning">
	            <strong>Error: </strong>${error }
	        </div>
        </c:if>

	</section>

</body>
</html>