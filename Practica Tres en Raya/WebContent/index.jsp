<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>

<%
	request.setAttribute("titulo-pagina", "Portada");
	request.setAttribute("iniciado", true);
%>

	<%@ include file="check-cookie.jsp" %>

	<%@ include file="layout/header.jsp" %>


		<div class="container">
			<div class="row">
				<div class="col-sm-7 col-sm-offset-1 col-xs-12" ng-controller="desafiosCtrl">
					<section id="mis-desafios" class="panel panel-primary" ng-show="desafiosEnviados.length > 0 || desafiosRecibidos.length > 0 || desafiosRechazados.length > 0 || desafiosAceptados.length > 0">
						<div class="panel-heading">
							<h2 class="panel-title">Desafíos</h2>
						</div>

						<div id="caja-desafios" class="panel-body">
							<div class="caja-desafio" ng-show="desafiosEnviados.length > 0" ng-class="{desafiosEnviados: active}">
								<h4 class="text-primary">Desafíos Enviados</h4>	

								<div ng-repeat="desafio in desafiosEnviados">
									<p><i class="fa fa-question-circle"></i> Ha enviado un desafío a {{ desafio.desafiado.nombre }}</p>
								</div>
							</div>

							<div class="caja-desafio" ng-show="desafiosRecibidos.length > 0" ng-class="{desafiosRecibidos: active}">
								<h4 class="text-primary">Desafíos Recibidos</h4>
								
								<div ng-repeat="desafio in desafiosRecibidos">
									<p><i class="fa fa-question-circle"></i> {{ desafio.desafiante.nombre }} le ha desafiado.</p>
									<button class="btn btn-xs btn-success" ng-click="aceptar($index, desafio)">Acepto el desafío</button>
									<button class="btn btn-xs btn-warning" ng-click="rechazar($index, desafio)">Rechazar</button>
								</div>
							</div>

							<div class="caja-desafio" ng-show="desafiosAceptados.length > 0" ng-class="{desafiosAceptados: active}">
								<h4 class="text-primary">Desafíos Aceptados</h4>
								
								<div ng-repeat="desafio in desafiosAceptados">
									<p><i class="fa fa-thumbs-o-up"></i> {{ desafio.desafiado.nombre }} ha aceptado su desafío</p>
									<button class="btn btn-xs btn-success" ng-click="ir(desafio.pid)">Ir a la Partida</button>
								</div>
							</div>

							<div class="caja-desafio" ng-show="desafiosRechazados.length > 0" ng-class="{desafiosRechazados: active}">
								<h4 class="text-primary">Desafíos Rechazados</h4>

								<div ng-repeat="desafio in desafiosRechazados">
									<p><i class="fa fa-thumbs-o-down"></i> {{ desafio.desafiado.nombre }} ha rechazado su desafío.</p>
									<button class="btn btn-xs btn-primary" ng-click="eliminarDesafio($index, desafio)">Aceptar</button>
								</div>
							</div>
						</div>
					</section>
				</div>
				
				<div class="col-sm-7 col-sm-offset-1 col-xs-12">
					<section class="caja-portada" ng-controller="salasCtrl">
						<h1><i class="fa fa-gamepad"></i> Partidas Activas</h1>
							<p ng-show="partidas.length === 0" class="text-danger">No hay partidas activas en este momento</p>
						<div ng-show="partidas.length > 0">
							<ul class="fa-ul">
								<li ng-repeat="partida in partidas" class="fa fa-hand-o-right index-desafios-item"><a href="javascript:void(0)" ng-click="ir(partida.pid)">{{partida.desafiante.nombre}} <i class="fa fa-bolt"></i> {{partida.desafiado.nombre}} </a></li>
							</ul>
						</div>
					</section>
				</div>

				<%@ include file="layout/barra-lateral.jsp" %>
			</div>
		</div>


	<%@ include file="layout/footer.jsp" %>	