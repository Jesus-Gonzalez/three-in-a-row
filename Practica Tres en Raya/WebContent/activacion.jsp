<%@page import="java.util.Map"%>
<%@page import="helpers.RecordarmeHelper"%>
<%@ page import="modelos.MActivaciones" %>
<%@ page import="modelos.MUsuarios" %>
<%@ page import="java.sql.Connection "%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>

<%
	request.setAttribute("titulo-pagina", "Activación");
	request.setAttribute("iniciado", true);
	request.setAttribute("forward", "index.jsp");
%>

<%@ include file="check-cookie.jsp" %>

<%
	String accion = request.getParameter("accion");
	String clave = request.getParameter("clave");
	String strAid = request.getParameter("id");
	boolean isReenviar = (accion != null && accion.equals("reenviar"));
	
	if (!isReenviar && (clave == null || strAid == null))
	{
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
		return;
	}
	
	int error = 0;
	
	if ( !isReenviar )
	{	
		Connection conexion = (Connection) session.getAttribute("conexion"); 
		MUsuarios mdlUsuarios = new MUsuarios(conexion);
		MActivaciones mdlActivaciones = new MActivaciones(conexion);
	
		try
		{
			long aid = Long.parseLong(strAid);
			
			if (mdlActivaciones.getActivacionByAid(aid))
			{
				mdlUsuarios.getUsuarioByUid(mdlActivaciones.uid);
				
				if (mdlUsuarios.getProximoUsuario())
				{
					if (!mdlUsuarios.activado)
					{
						if (mdlActivaciones.clave.equals(clave))
						{
							mdlUsuarios.activado = true;
							mdlUsuarios.actualizaUsuario();
							
							mdlActivaciones.borraActivacion();
							
						} else {
							
							error = 4; // La clave no coincide
						}
						
					} else {
						
						error = 3; // Cuenta ya activada
					}
					
				} else {
					error = 2; // No existe usuario
				}
				
			} else {
				error = 1; // No existe dicha activacion
			}
				
		} catch (NumberFormatException x) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}
%>

	<%@ include file="layout/header.jsp" %>

		<div class="container">
			<div class="row">
				<div class="col-sm-7 col-sm-offset-1 col-xs-12">
					<section id="seccion-login" class="caja-login">
						<div class="col-sm-10 col-xs-12">
							<h1>Activación de Cuenta</h1>
							
							<%
								if (!isReenviar)
								{
							%>
								
								<%
									if (error == 0)
									{
								%>
									<div class="panel panel-success">
										<div class="panel-heading">
											<h2 class="panel-title">Cuenta Activada con Éxito</h2>
										</div>
										<div class="panel-body">
											<p>Su cuenta se ha activado con éxito.</p>
											<p>Ahora puede proceder a <a href="login.jsp">identificarse</a>.</p>
										</div>
									</div>
								<%
									} else if (error == 1) {
								%>
									<div class="panel panel-danger">
										<div class="panel-heading">
											<h2 class="panel-title">Activación Inexistente</h2>
										</div>
										<div class="panel-body">
											<p>No se ha encontrado una activación para este identificador.</p>
											<p>Probablemente su cuenta ya se encuentre activada.</p>
										</div>
									</div>
								<%
									} else if (error == 2) {
								%>
									<div class="panel panel-danger">
										<div class="panel-heading">
											<h2 class="panel-title">No Existe el Usuario</h2>
										</div>
										<div class="panel-body">
											<p>Está intentando activar una cuenta para un usuario inexistente.</p>
										</div>
									</div>
								<%
									} else if (error == 3) {
								%>
									<div class="panel panel-danger">
										<div class="panel-heading">
											<h2 class="panel-title">Cuenta ya Activada</h2>
										</div>
										<div class="panel-body">
											<p>La cuenta que desea activar ya se encuentra activada.</p>
										</div>
									</div>
								<%
									} else if (error == 4) {
								%>
									<div class="panel panel-danger">
										<div class="panel-heading">
											<h2 class="panel-title">Clave Incorrecta</h2>
										</div>
										<div class="panel-body">
											<p>La clave de activación no es correcta.</p>
										</div>
									</div>
								<%
									}
								%>
							<%
								} else {
							%>
								<div class="col-sm-8 col-xs-12">
									<div class="panel panel-primary">
										<div class="panel-heading">
											<h2 class="panel-title">Reenviar Clave de Activación</h2>
										</div>
										<div class="panel-body">
											<form name="formulario-reenvio" id="formulario-reenvio" action="reenviar" method="POST">
												<div class="form-group">
													<label for="correo">Correo electrónico</label>
													<input type="email" class="form-control" id="correo" name="correo" placeholder="mi.nombre@gmail.com">
												</div>
												
												<div class="form-group">
													<input type="submit" class="btn btn-md btn-primary" name="btn-reenviar" id="btn-reenviar" value="Reenviar Clave">
												</div>
											</form>
										</div>
									</div>
								</div>
							<%
								}
							%>
						</div>
					</section>
				</div>
				
				<%@ include file="layout/barra-lateral.jsp" %>
			</div>
		</div>


	<%@ include file="layout/footer.jsp" %>