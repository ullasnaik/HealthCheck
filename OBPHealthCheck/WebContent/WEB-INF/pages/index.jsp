<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>Oracle Banking Platform</title>
<spring:url value="/resources/style.css" var="styleCSS" />
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
</style>
<body>
<%
response.setIntHeader("Refresh", 300);
SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss");
String lastreftime = sdf.format(new Date());
%>
	<div id="content">
		<h2>Oracle Banking Platform</h2>

		<div style="text-align: center;">
			<div style="float: left;">
				<button class="button">
					<a href="<c:url value = "/DsStatus.html"/>">DataSorces & HeapSize</a>
				</button>
			</div>
			<div>
				<p>
					Last Refresh: <%=lastreftime%></p>
			</div>
		</div>
		<div class="flex-container" align="center">


			<table class="table3">
				<thead>
					<tr>
						<th class="center-align" scope="col" abbr="Business">Component</th>
						<th class="center-align" scope="col" abbr="Medium">Weblogic
							Server Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="envdtls" items="${servermap}">
						<tr>
							<td>${envdtls.key}</td>
							<td align="center">
								<table width="100%">
									<thead>
										<th>Server name</th>
										<th>State</th>
										<th>Application Name</th>
										<th>State</th>
									</thead>
									<tr>
										<c:forEach var="serverlistrow" items="${envdtls.value}">
											<tr>
												<c:forEach var="serverlist" items="${serverlistrow}"
													varStatus="loop">
													<c:choose>
														<c:when test="${loop.index=='0'}">
															<td class="serverID" align="center">${serverlist}</td>
														</c:when>
														<c:when test="${loop.index=='2'}">
															<td class="applicarionID">${serverlist}</td>
														</c:when>
														<c:when test="${loop.index=='1'}">
															<c:choose>
																<c:when test="${serverlist.equals('RUNNING')}">
																	<td class="appID" align="center"
																		style="color: green; text-transform: initial;">${serverlist}</td>
																</c:when>
																<c:when
																	test="${serverlist.equals('SHUTDOWN') or serverlist.equals('UNABLE_TO_CONNECT')}">
																	<td class="appID" align="center"
																		style="color: red; text-transform: initial;">${serverlist}</td>
																</c:when>
																<c:otherwise>
																	<td class="appID" align="center"
																		style="color: yellow; text-transform: initial;">${serverlist}</td>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:when test="${loop.index=='3'}">
															<c:choose>
																<c:when test="${serverlist.equals('STATE_ACTIVE')}">
																	<td class="appID" align="center"
																		style="color: green; text-transform: initial;">${serverlist}</td>
																</c:when>
																<c:when
																	test="${serverlist.equals('SHUTDOWN') or serverlist.equals('UNABLE_TO_CONNECT')}">
																	<td class="appID" align="center"
																		style="color: red; text-transform: initial;">${serverlist}</td>
																</c:when>
																<c:otherwise>
																	<td class="appID" align="center"
																		style="color: yellow; text-transform: initial;">${serverlist}</td>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>
															<td class="appID">${serverlist}</td>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</tr>
										</c:forEach>
									</tr>
								</table>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<br></br>
	</div>
	<br></br>
</body>
</html>