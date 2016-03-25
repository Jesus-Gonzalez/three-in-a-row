<%@page import="modelos.SesionUsuario"%>

<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%
	// Si se realiza una petición directa, devolver error 403 Prohibido
	if ((Boolean) request.getAttribute("iniciado") == null)
	{
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

	SesionUsuario sesion = (SesionUsuario) session.getAttribute("sesion");
	
%>

<!DOCTYPE html>
<html ng-app="tresenraya">
<head>
	<meta charset="utf-8">
	<title><%= request.getAttribute("titulo-pagina") %></title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<link rel="stylesheet" href="css/bootstrap-theme.min.css">
	<link rel="stylesheet" href="css/font-awesome.min.css">
	<link rel="stylesheet" href="css/flag-icon.min.css">
	<!-- <link rel="stylesheet" href="css/flags.css"> -->
	<link rel="stylesheet" href="css/less/main.min.css">
</head>
<body>
	<header class="container-fluid">
		<nav class="navbar navbar-default" role="navigation">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
					<span class="sr-only">Desplegar Menú</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="index.jsp">Tres en Raya</a>
			</div>
		
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav navbar-right">
					<% if (sesion.estado == SesionUsuario.CONECTADO) { %>
					<li><a href="registro.jsp">Registrarse</a></li>
					<li><a href="login.jsp">Iniciar Sesión</a></li>
					<% } else { %>
					<li><a href="usuarios/desconectar">Desconectar</a></li>
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown"><%= sesion.usuario.nombre %> <b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="perfil.jsp">Mis Ajustes</a></li>
							<li><a href="usuarios/desconectar">Salir</a></li>
						</ul>
					</li>
					<% } %>
				</ul>
			</div><!-- /.navbar-collapse -->
		</nav>
	</header>