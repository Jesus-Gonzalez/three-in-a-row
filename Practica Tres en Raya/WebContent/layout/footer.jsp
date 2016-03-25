<%@page import="utils.CookiesUtils"%>
<%@page import="utils.HashMapCookie"%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="datos" %>

<%
	// Si se realiza una petición directa, devolver error 403 Prohibido
	if ( (Boolean) request.getAttribute("iniciado") == null )
	{
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

	HashMapCookie<String, String> cookieMap = new CookiesUtils().createCookieMapFromArray("tresenraya", request.getCookies());
	
	String strCookieAcepta = cookieMap.get("cookies.acepta");
	boolean aceptaCookies = strCookieAcepta != null && strCookieAcepta.equals("true");
	
%>

	<hr>
	
	<div class="container">
		<div class="row">
			<div class="col-sm-5 col-sm-offset-1 col-xs-12">
				<h2><i class="fa fa-users"></i> Usuarios Conectados</h2>
				<p>Usuarios Conectados: <datos:conectados/></p>
				<p>Usuarios Registrados Conectados: <datos:registradosConectados/></p>
			</div>

			<div class="col-sm-5 col-xs-12">
				<h2><i class="fa fa-gamepad"></i> Partidas Activas</h2>
				<p>Partidas actualmente activas: <datos:partidasActivas/></p>
			</div>
		</div>
	</div>

	<% if (!aceptaCookies) { %>

	<nav id="nav-alerta-cookie" class="navbar navbar-default navbar-fixed-bottom" role="alert" aria-expanded="true">
		<div class="container">
			<h2>Alerta</h2>
			<p>Este sitio utiliza cookies para su funcionamiento. Si continúa navegando aceptará el uso de cookies.</p>
			<p>Háganos saber si acepta el uso de cookies con el uso de los botones siguientes.</p>
			<button id="aceptar-alerta-cookie" class="btn btn-md btn-primary" role="button">Aceptar</button>
			<a href="rechazarcookie" class="btn btn-md btn-warning" role="link">No Acepto</a>
		</div>
	</nav>

	<%
	
		cookieMap.put("cookies.acepta", "true");
		Cookie c = new Cookie("tresenraya", cookieMap.toString());
		c.setPath("/");
		c.setMaxAge(31536000); // 1 año = 31536000 segundos
		response.addCookie(c);

	 }
	
	%>

	<script type="text/javascript" charset="utf-8" src="js/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="js/angular.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="js/scripts.js"></script>
	<script type="text/javascript" charset="utf-8" src="js/angular/app.js"></script>
	<script type="text/javascript" charset="utf-8" src="js/angular/olvido.js"></script>
	<script type="text/javascript" charset="utf-8" src="js/angular/salas.js"></script>
	<script type="text/javascript" charset="utf-8" src="js/angular/perfil.js"></script>
	<script type="text/javascript" charset="utf-8" src="js/angular/desafios.js"></script>
	<script type="text/javascript" charset="utf-8" src="js/angular/usuarios.js"></script>
	<script type="text/javascript" charset="utf-8" src="js/angular/partidas.js"></script>
</body>
</html>