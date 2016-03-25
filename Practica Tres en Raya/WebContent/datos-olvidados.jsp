<%@page import="helpers.RecordarmeHelper"%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>

<%
	request.setAttribute("titulo-pagina", "Olvidé mi usuario/contraseña");
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

							<div id="alert-olvido-success" class="alert alert-success alert-box hidden">
								<strong>Éxito!</strong>
								<p>Se ha enviado un correo electrónico con los datos que ha pedido.</p>
							</div>

							<div id="form-olvido" ng-controller="olvidoCtrl">
								<div class="form-group">
									<label for="nombre">Correo Electrónico</label>
									<input class="form-control" type="email" name="email" id="email" ng-model="email" placeholder="ejemplo@gmail.com">
									<div id="olvido-alert-correo" class="alert alert-danger alert-box hidden">
										<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
										<strong>Error!</strong>
										<p>El correo electrónico introducido no existe</p>
									</div>
								</div>

								<div class="form-group">
									<p><button class="btn btn-primary" ng-click="enviarUsuario()">Olvidé mi nombre de usuario</button></p>
									<p><button class="btn btn-primary" ng-click="enviarContrasena()">Olvidé mi contraseña</button></p>
								</div>
							</div>
						</div>
					</section>
				</div>

				<%@ include file="layout/barra-lateral.jsp" %>
			</div>
		</div>


	<%@ include file="layout/footer.jsp" %>