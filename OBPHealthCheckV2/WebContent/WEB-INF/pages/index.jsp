<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>Oracle Banking Platform</title>

<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="description" content="Oracle Banking Platform" />
<meta name="keywords"
	content="table, css3, style, beautiful, fancy, css" />
</head>
<style>
#content h2 {
	font-family: "Trebuchet MS", sans-serif;
	text-align: center;
	font-size: 34px;
	font-style: normal;
	background-color: #8cc2e6;
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss a");
		String lastreftime = sdf.format(new Date());
	%>
	<div id="content">
		<h2>Oracle Banking Platform - ${environmentName}</h2>
	</div>

		<div style="width: 50%; float: left;">
			<p style="font: bold; color: white;">
				Last Refresh :
				<%=lastreftime%></p>
		</div>
		<div style="width: 50%; float: right;">
			<button>
				<a href="<c:url value = "/DsStatus.html"/>">DataSorces &
					HeapSize</a>
			</button>
		</div>


	<div style="width: 100%; float: left;">
		<table>
			<thead>
				<tr>
					<th>Component</th>
					<th>Server Status</th>
					<th>Application Status</th>
				</tr>
				<thead>
					<tbody>
						<c:forEach var="component" items="${ServerDetailsList}">
							<tr>
								<td align="center">${component.servername}</td>
								<td align="center">
									<table width="100%">
										<c:forEach var="domains" items="${component.domainStat}">
											<tr>
												<td><c:out value="${domains.key}" /></td>
												<c:choose>
													<c:when test="${domains.value.equals('RUNNING')}">
														<td align="right"
															style="color: green; text-transform: initial;">${domains.value}</td>
													</c:when>
													<c:when
														test="${domains.value.equals('OVERLOADED') or domains.value.equals('FAILED') or domains.value.equals('CRITICAL') or domains.value.equals('SHUTDOWN') or domains.value.equals('UNABLE TO CONNECT')}">
														<td align="right"
															style="color: red; text-transform: initial;">${domains.value}</td>
													</c:when>
													<c:otherwise>
														<td align="right"
															style="color: yellow; text-transform: initial;">${domains.value}</td>
													</c:otherwise>
												</c:choose>
											</tr>
										</c:forEach>
									</table>
								</td>
								<td>
									<table width="100%">
										<tbody>
											<c:forEach var="deployments"
												items="${component.deploymentStat}" varStatus="loop">
												<tr>
													<td><c:out value="${deployments.key}" /></td>
													<c:choose>
														<c:when test="${deployments.value.equals('OK')}">
															<td align="right"
																style="color: green; text-transform: initial;">${deployments.value}</td>
														</c:when>
														<c:when
															test="${deployments.value.equals('FAILED') or deployments.value.equals('CRITICAL') or deployments.value.equals('OVERLOADED')}">
															<td align="right"
																style="color: red; text-transform: initial;">${deployments.value}</td>
														</c:when>
														<c:otherwise>
															<td align="right"
																style="color: yellow; text-transform: initial;">${deployments.value}</td>
														</c:otherwise>
													</c:choose>
												</tr>
											</c:forEach>
										</tbody>
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