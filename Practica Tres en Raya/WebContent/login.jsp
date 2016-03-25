<%@page import="helpers.RecordarmeHelper"%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>

<%
	request.setAttribute("titulo-pagina", "Identificarse");
	request.setAttribute("iniciado", true);
	request.setAttribute("forward", "index.jsp");
%>

	<%@ include file="check-cookie.jsp" %>

	<%@ include file="layout/header.jsp" %>

		<div class="container">
			<div class="row">
				<div class="col-sm-7 col-sm-offset-1 col-xs-12">
					<section id="seccion-login" class="caja-login">
						<div class="col-sm-5 col-xs-12">
							<h1>Identificarse</h1>

							<div id="registro-alert-ya-conectado" class="alert alert-danger hidden">
								<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
								<strong>Error!</strong>
								<p>No puede iniciar sesión:</p>
								<p>El usuario ya está conectado</p>
							</div>

							<form action="usuarios/login" name="formulario-login" id="formulario-login">
								<div class="form-group">
									<label for="nombre">Nombre de Usuario</label>
									<input class="form-control" type="text" name="nombre" id="nombre">
								</div>

								<div class="form-group">
									<label for="contrasena">Contraseña</label>
									<input class="form-control" type="password" name="contrasena" id="contrasena">
								</div>
								
								<div class="checkbox">
									<label for="recordar">
										<input type="checkbox" name="recordar" id="recordar"> Recordarme
									</label>
								</div>

								<div class="form-group">
									<input type="submit" class="btn btn-lg btn-success">
								</div>

								<div class="form-group">
									<p><a href="registro.jsp" class="btn btn-md btn-primary">Registrarse</a></p>
									<p><a href="activacion.jsp?accion=reenviar" class="btn btn-sm btn-primary">Reenviar clave activación</a></p>
									<p><a href="datos-olvidados.jsp" class="btn btn-sm btn-danger">Olvidé mi nombre de usuario</a></p>
									<p><a href="datos-olvidados.jsp" class="btn btn-sm btn-danger">Olvidé mi contraseña</a></p>
								</div>
							</form>
						</div>
					</section>
				</div>

				<%@ include file="layout/barra-lateral.jsp" %>
			</div>
		</div>


	<%@ include file="layout/footer.jsp" %>