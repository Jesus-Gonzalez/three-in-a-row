<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>

<%
	// Si se realiza una petición directa, devolver error 403 Prohibido
	if ( (Boolean) request.getAttribute("iniciado") == null )
	{
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
%>

<aside id="sidebar" class="col-sm-3 col-sm-offset-1 col-xs-12">
	<div class="caja caja-lateral caja-usuarios-conectados" ng-controller="usuariosCtrl">
		<h4>Usuarios Conectados</h4>
		
		<p class="text-danger" ng-show="usuarios.length === 0">No hay usuarios conectados</p>

		<ul id="lista-usuarios">
			<li class="lista-usuarios-item" ng-repeat="usuario in usuarios">
				<img src="img/blank.png" alt="bandera {{ usuario.pais }}" class="flag flag-{{ usuario.pais }}">
				<a href="javascript:void(0)" class="link-usuario">{{ usuario.nombre }}</a>
				<div class="lista-usuarios-acciones">
					<button ng-click="desafiar(usuario.uid)" class="btn btn-xs btn-success">Desafiar</button>
					<button ng-click="verPerfil(usuario.uid)" class="btn btn-xs btn-primary">Ver Perfil</button>
				</div>
			</li>
		</ul>
 	</div>
</aside>