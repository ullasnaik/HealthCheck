<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>Oracle Banking Platform</title>
<spring:url value="/resources/style2.css" var="styleCSS" />
<link href="${styleCSS}" rel="stylesheet" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="description" content="Oracle Banking Platform" />
<meta name="keywords"
	content="table, css3, style, beautiful, fancy, css" />
</head>
<style>
* {
	margin: 0;
	padding: 0;
}

body {
	font-family: Georgia, serif;
	font-style: italic;
	font-weight: normal;
	letter-spacing: normal;
	background: #f0f0f0;
}

#content {
	background-color: #fff;
	margin: 0 auto;
	border-left: 10px solid #1D81B6;
	border-right: 1px solid #ddd;
	-moz-box-shadow: 0px 0px 16px #aaa;
}

.head {
	font-family: Helvetica, Arial, Verdana;
	text-transform: uppercase;
	font-weight: bold;
	font-size: 12px;
	font-style: normal;
	letter-spacing: 3px;
	color: #888;
	border-bottom: 3px solid #f0f0f0;
	padding-bottom: 10px;
	margin-bottom: 10px;
}

.head a {
	color: #1D81B6;
	text-decoration: none;
	float: right;
	text-shadow: 1px 1px 1px #888;
}

.head a:hover {
	color: #f0f0f0;
}

#content h2 {
	font-family: "Trebuchet MS", sans-serif;
	text-align: center;
	font-size: 34px;
	font-style: normal;
	background-color: #f0f0f0;
	margin: 0px 0px 1px -0px;
	padding: 0px 40px;
	clear: both;
	float: left;
	width: 100%;
	color: #0f24c1;
	text-shadow: 1px 1px 1px #fff;
}

#content button {
	text-align: center;
	align: center;
}

.container {
	width: auto;
	height: auto;
}

.leftpane {
	width: 50%;
	height: 100%;
	float: left;
	position: right;
	border-collapse: collapse;
	overflow-x: auto;
}

.rightpane {
	width: 50%;
	height: 100%;
	position: left;
	float: right;
	border-collapse: collapse;
	overflow-x: auto;
}
</style>
<body>

	<%
		response.setIntHeader("Refresh", 300);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss");
		String lastreftime = sdf.format(new Date());
	%>
	<div id="content">
		<div class="flex-container">
			<h2>Oracle Banking Platform</h2>
			
			<div style="text-align: center;">
				<div style="float: left;">
					<button class="button">
						<a href="<c:url value = "/home.html"/>">HOME</a>
					</button>
				</div>
				<div><p>Last Refresh: <%=lastreftime%></p></div>
			</div>
			<div class="container">
				<div class="leftpane" align="center">
					<table class="table3">
						<thead>
							<tr>
								<th class="center-align" scope="col" abbr="Business">Component</th>
								<th class="center-align" scope="col" abbr="Medium">DataSource
									Status</th>

							</tr>
						</thead>
						<tbody>
							<c:forEach var="dsenvdtls" items="${DSList}">
								<tr>
									<td>${dsenvdtls.key}</td>
									<td align="center">
										<table width="100%">
											<thead>
												<th class="datasourceID">DataSource Name</th>
												<th>State</th>
												<th>AvgCnt</th>
												<th>CurCnt</th>
												<th>HighCnt</th>
											</thead>

											<c:forEach var="DSserverlist" items="${dsenvdtls.value}">
												<tr>
													<c:forEach var="DSserverlistDtls" items="${DSserverlist}"
														varStatus="loop">
														<c:choose>
															<c:when test="${loop.index=='1'}">
																<c:choose>
																	<c:when test="${DSserverlistDtls.equals('Running')}">
																		<td align="center"
																			style="color: green; text-transform: initial;">${DSserverlistDtls}</td>
																	</c:when>
																	<c:when
																		test="${DSserverlistDtls.equals('SHUTDOWN') or DSserverlistDtls.equals('UNABLE_TO_CONNECT')}">
																		<td align="center"
																			style="color: red; text-transform: initial;">${DSserverlistDtls}</td>
																	</c:when>
																	<c:otherwise>
																		<td align="center"
																			style="color: yellow; text-transform: initial;">${DSserverlistDtls}</td>
																	</c:otherwise>
																</c:choose>
															</c:when>
															<c:when test="${loop.index=='2'}">
																<td align="center"
																	title="Active Connections Average Count">${DSserverlistDtls}</td>
															</c:when>
															<c:when test="${loop.index=='3'}">
																<td align="center"
																	title="Active Connections Current Count">${DSserverlistDtls}</td>
															</c:when>
															<c:when test="${loop.index=='4'}">
																<td align="center" title="Active Connections High Count">${DSserverlistDtls}</td>
															</c:when>
															<c:otherwise>
																<td align="center">${DSserverlistDtls}</td>
															</c:otherwise>
														</c:choose>


													</c:forEach>
												</tr>
											</c:forEach>
										</table>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>




				<div class="rightpane" align="center">
					<table class="table3">
						<thead>
							<tr>
								<th class="center-align" scope="col" abbr="Business">Component</th>
								<th class="center-align" scope="col" abbr="Medium">Heap&Thread
									Status:</th>

							</tr>
						</thead>
						<tbody>
							<c:forEach var="HTenvdtls" items="${HTList}">
								<tr>
									<td>${HTenvdtls.key}</td>
									<td align="center">
										<table width="100%">
											<thead>
												<th class="datasourceID">Serever Name</th>
												<th title="Size in GB">HeapUsed</th>
												<th title="Size in GB">HeapFree</th>
												<th>Idle</th>
												<th>Pndg</th>
												<th>Hogng</th>
												<th>Stdby</th>
											</thead>
											<c:forEach var="HTserverlist" items="${HTenvdtls.value}">
												<tr>
													<c:forEach var="HTserverlistDtls" items="${HTserverlist}"
														varStatus="loop">
														<c:choose>
															<c:when test="${loop.index=='1'}">
																<td align="center" title="Size in GB">${HTserverlistDtls}</td>
															</c:when>
															<c:when test="${loop.index=='2'}">
																<td align="center" title="Size in GB">${HTserverlistDtls}</td>
															</c:when>
															<c:otherwise>
																<td align="center">${HTserverlistDtls}</td>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</tr>
											</c:forEach>
										</table>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<br>
	</div>

	<br>
</body>
</html>