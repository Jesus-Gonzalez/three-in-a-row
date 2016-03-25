<%@page import="modelos.Partida"%>
<%@page import="java.util.HashMap"%>
<%@page import="modelos.MUsuarios"%>
<%@page import="modelos.MPartidas"%>
<%@page import="java.sql.Connection"%>
<%@page import="helpers.RecordarmeHelper" %>
<%@page import="modelos.SesionUsuario"%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>

<%!
	boolean existePartida = false;
%>

<%
	SesionUsuario s = (SesionUsuario) session.getAttribute("sesion");
	Connection conexion = (Connection) session.getAttribute("conexion");
	MPartidas mdlPartidas = new MPartidas(conexion);
	MUsuarios mdlUsuarios = new MUsuarios(conexion);
	String pid = request.getParameter("pid");
	HashMap<String, Partida> mapaPartidas = (HashMap<String, Partida>) application.getAttribute("partidas.activas");
	
	// existePartida = true;
	
	if (pid != null)
	{
		Partida partida = mapaPartidas.get(pid);
		
		if (partida != null && partida.estado != Partida.DESAFIADO)
		{
			existePartida = true;
		}
	}
	
	if (!existePartida)
	{
		response.sendRedirect("index.jsp");
	}

	request.setAttribute("titulo-pagina", "Partida");
	request.setAttribute("iniciado", true);
%>

	<%@ include file="check-cookie.jsp" %>

	<%@ include file="layout/header.jsp" %>

		<div class="container">
			<div class="row">
				<% if (!existePartida) { %>
				<div class="caja caja-juego col-sm-10 col-sm-offset-1 col-xs-12">
					<h2 class="text-danger text-center">Error: Esta partida no existe.</h2>
				</div>
				<% } else { %>
				<div class="caja caja-juego col-sm-10 col-sm-offset-1 col-xs-12">
					<section id="seccion-partida" class="container-fluid" ng-controller="partidaCtrl">
						<div class="row">
							<h3 class="text-center">{{ desafiante.nombre }} VS {{ desafiado.nombre }}</h3>
							
							<h3 class="text-center text-primary" ng-show="resultado === 0">¡Tenemos un empate! <i class="fa fa-shield"></i></h3>
							<h3 class="text-center text-success" ng-show="resultado === 1">¡Tenemos un ganador! <i class="fa fa-trophy"></i></h3>
							<h4 class="text-center" ng-show="estado === 2">Fin de la partida</h2>

							<div id="jugador-desafiante" class="col-md-3 col-xs-12">
								<h4>{{ desafiante.nombre }} <span class="flag-icon flag-icon-{{ desafiante.pais }}"></span></h4>
								<h5 class="text-success" ng-show="desafiante.ganador">GANADOR! <i class="fa fa-trophy"></i></h5>
								<p>Partidas Ganadas: {{ desafiante.ganadas }}</p>
								<p>Partidas Perdidas: {{ desafiante.perdidas }}</p>
								<p>Partidas Empatadas: {{ desafiante.empatadas }}</p>
								<strong class="pull-right text-success" ng-show="desafiante.turno"><i class="fa fa-arrow-right"></i> Su Turno</strong>
							</div>

							<div id="jugador-desafiado" class="col-md-3 col-md-push-6 col-xs-12">
								<h4><span class="flag-icon flag-icon-{{ desafiado.pais }}"></span> {{ desafiado.nombre }}</h4>
								<h5 class="text-success" ng-show="desafiado.ganador">GANADOR! <i class="fa fa-trophy"></i></h5>
								<p>Partidas Ganadas: {{ desafiado.ganadas }}</p>
								<p>Partidas Perdidas: {{ desafiado.perdidas }}</p>
								<p>Partidas Empatadas: {{ desafiado.empatadas }}</p>
								<strong class="pull-left text-success" ng-show="desafiado.turno">Su Turno <i class="fa fa-arrow-left"></i></strong>
							</div>

							<div class="col-md-6 col-md-pull-2 col-xs-12" id="tablero-juego">
								<ul>
									<li ng-class="{'fa fa-5x fa-times': movimientos[0][0] === -1, 'fa fa-5x fa-circle-o': movimientos[0][0] === 1}" ng-click="mover(1)"></li>
									<li ng-class="{'fa fa-5x fa-times': movimientos[0][1] === -1, 'fa fa-5x fa-circle-o': movimientos[0][1] === 1}" ng-click="mover(2)"></li>
									<li ng-class="{'fa fa-5x fa-times': movimientos[0][2] === -1, 'fa fa-5x fa-circle-o': movimientos[0][2] === 1}" ng-click="mover(3)"></li>
								</ul>

								<ul>
									<li ng-class="{'fa fa-5x fa-times': movimientos[1][0] === -1, 'fa fa-5x fa-circle-o': movimientos[1][0] === 1}" ng-click="mover(4)"></li>
									<li ng-class="{'fa fa-5x fa-times': movimientos[1][1] === -1, 'fa fa-5x fa-circle-o': movimientos[1][1] === 1}" ng-click="mover(5)"></li>
									<li ng-class="{'fa fa-5x fa-times': movimientos[1][2] === -1, 'fa fa-5x fa-circle-o': movimientos[1][2] === 1}" ng-click="mover(6)"></li>
								</ul>

								<ul>
									<li ng-class="{'fa fa-5x fa-times': movimientos[2][0] === -1, 'fa fa-5x fa-circle-o': movimientos[2][0] === 1}" ng-click="mover(7)"></li>
									<li ng-class="{'fa fa-5x fa-times': movimientos[2][1] === -1, 'fa fa-5x fa-circle-o': movimientos[2][1] === 1}" ng-click="mover(8)"></li>
									<li ng-class="{'fa fa-5x fa-times': movimientos[2][2] === -1, 'fa fa-5x fa-circle-o': movimientos[2][2] === 1}" ng-click="mover(9)"></li>
								</ul>
							</div>
						</div>
						
						<h2 class="text-center" ng-show="estado === 1">
							{{ segundosRestantes }}
						</h2>

						<div class="row">
							<div class="text-center">
								<a class="btn btn-lg btn-success" ng-show="estado === 2" ng-click="reiniciar()"><i class="fa fa-magic"></i> Reiniciar Partida</a>
								<a class="btn btn-lg btn-danger" ng-show="estado === 2" ng-click="terminar()"><i class="fa fa-magic"></i> Terminar Partida</a>
							</div>
							<div class="col-sm-8 col-sm-offset-2 col-xs-12">
								<h4><i class="fa fa-gamepad"></i> Datos de la Partida</h4>
								<p>{{ partidasJugadas }} Partida(s) Jugadas</p>
							</div>
						</div>
					</section>
				</div>
				<% } %>
			</div>
		</div>


	<%@ include file="layout/footer.jsp" %>