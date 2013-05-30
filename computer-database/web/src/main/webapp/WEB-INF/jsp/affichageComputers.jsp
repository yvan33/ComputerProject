<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Computers database</title>
<link rel="stylesheet" type="text/css" media="screen"
	href="style/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="style/main.css">
</head>
<body>

	<header class="topbar">
		<h1 class="fill">
			<a href="affichageComputers.html"> My super Computer database </a>
		</h1>
	</header>

	<section id="main">


		<h1>${page.total eq 0 ? 'No': page.total } computers found</h1>
		<c:if test="${! empty info }">
			 <div class="alert-message warning">
	            <strong>Done ! </strong>${info }
	        </div>
        </c:if>
				<div id="actions">

					<form action="affichageComputers.html" method="GET">

						<input type="search" id="searchbox" name="f"
							value="${filter }"
							placeholder="Filter by computer name..."> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn primary">

					</form>


					<a class="btn success" id="add" href="ajoutComputer.html">Add a new
						computer</a>

				</div>

		<c:choose>
			<c:when test="${page.total == 0}">
				

				<div class="well">
					<em>Nothing to display</em>
				</div>
			</c:when>
			<c:otherwise>

			
				<table class="computers zebra-striped">
					<thead>
						<tr>

							<th
								class="col2 header ${tri eq '2' ? 'headerSortUp': tri eq '-2' ? 'headerSortDown': empty tri ? 'headerSortUp': ''}">
								<a
								href="affichageComputers.html?f=${filter}&s=${tri eq '2' ? '-2': '2'} ">Computer
									name</a>
							</th>


							<th
								class="col3 header ${tri eq '3' ? 'headerSortUp': tri eq '-3' ? 'headerSortDown': ''} ">
								<a
								href="affichageComputers.html?f=${filter}&s=${tri eq '3' ? '-3': '3'}">Introduced</a>
							</th>


							<th
								class="col4 header ${tri eq '4' ? 'headerSortUp':tri eq '-4' ? 'headerSortDown': ''}">
								<a
								href="affichageComputers.html?f=${filter}&s=${tri eq '4' ? '-4': '4'}">Discontinued</a>
							</th>


							<th
								class="col5 header ${tri eq '5' ? 'headerSortUp':tri eq '-5' ? 'headerSortDown': ''}">
								<a
								href="affichageComputers.html?f=${filter}&s=${tri eq '5' ? '-5': '5'}">Company</a>
							</th>

						</tr>

					</thead>
					<tbody>

						<c:forEach items="${page.computers}" var="computer">
							<tr>
								<td><a href="editionComputer.html?id=${computer.id}">${computer.name}</a></td>
								<td><c:if test="${empty computer.introduced}">
										<em>-</em>
									</c:if> <c:if test="${not empty computer.introduced}">${computer.introduced }</c:if></td>
								<td><c:if test="${empty computer.discontinued}">
										<em>-</em>
									</c:if> <c:if test="${not empty computer.discontinued}">${computer.discontinued }</c:if></td>
								<td><c:if test="${empty computer.company.name}">
										<em>-</em>
									</c:if> <c:if test="${not empty computer.company.name}">${computer.company.name }</c:if></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<div id="pagination" class="pagination">
					<ul>
						<c:if test="${page.first}">
							<li class="prev disabled"><a>&larr; Previous</a></li>
						</c:if>
						<c:if test="${!page.first}">
							<li class="prev"><a
								href="affichageComputers.html?s=${tri}&f=${filter}&page=${page.pageNumber -1}">&larr;
									Previous</a></li>
						</c:if>

						<li class="current"><a>Displaying ${page.displayFrom} to
								${page.displayTo} of ${page.total }</a></li>

						<c:if test="${!page.last}">
							<li class="next"><a
								href="affichageComputers.html?s=${tri}&f=${filter}&page=${page.pageNumber +1}">Next
									&rarr;</a></li>
						</c:if>
						<c:if test="${page.last}">
							<li class="next disabled"><a>Next &rarr;</a></li>
						</c:if>
					</ul>
				</div>


			</c:otherwise>
		</c:choose>


	</section>

</body>
</html>