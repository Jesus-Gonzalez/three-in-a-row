package controladores.servlets;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import helpers.RecordarmeHelper;
import modelos.MUsuarios;

@WebServlet("/usuarios/activacion/reenviar")
public class ReenviarActivacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		HttpSession sesion = request.getSession(false);
		
		if (sesion == null)
			return;
		
		RecordarmeHelper hlpRecordarme = new RecordarmeHelper();
		if (hlpRecordarme.comprobarCookieRecordarme(request, response, sesion))
		{
			response.sendRedirect(getServletContext().getContextPath() + "/index.jsp");
			return;
		}

		Connection conexion = (Connection) sesion.getAttribute("conexion");
		
		String correo = request.getParameter("correo");
		
		JsonObject  json = new JsonObject(),
				jsonErrores = new JsonObject();
	
		
		if (correo != null)
		{
			if (!correo.isEmpty())
			{
				if (correo.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
				{
					MUsuarios mdlUsuarios = new MUsuarios(conexion);
					
					mdlUsuarios.getUsuarioByCorreo(correo);
					
					if (mdlUsuarios.getProximoUsuario())
					{
						if (!mdlUsuarios.activado)
						{
							// TODO Reenviar correo
							
							json.addProperty("success", true);
							
						} else {
							jsonErrores.addProperty("activado", true);
							json.add("error", jsonErrores);
						}
						
					} else {
						
						jsonErrores.addProperty("existe", true);
						json.add("error", jsonErrores);
					}
			
				} else {
				
					jsonErrores.addProperty("formato", true);
					json.add("error", jsonErrores);
				}
			
			} else {
				
				jsonErrores.addProperty("vacio", true);
				json.add("error", jsonErrores);
			}
			
		} else {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		response.getWriter().write(json.toString());
	}

}
