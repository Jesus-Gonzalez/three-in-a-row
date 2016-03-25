package controladores.servlets;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import modelos.MUsuariosConectados;

/**
 * Servlet implementation class UsuariosConectados
 */
@WebServlet("/usuarios/conectados")
public class UsuariosConectados extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		
		if (session == null)
			return;
		
		MUsuariosConectados mdlUsuariosConectados = new MUsuariosConectados((Connection) session.getAttribute("conexion"));
		
		JsonParser jParser = new JsonParser();
		
		JsonObject  jsonRequest = new JsonObject(),
					json = new JsonObject(),
					jsonUsuario;
		
		JsonArray 	jsonArrUsuarios = new JsonArray();
		
		JsonElement jsonTipo;
		String tipoRequest;

		jsonRequest = jParser.parse(new JsonReader(request.getReader())).getAsJsonObject();
		
		if ( (jsonTipo = jsonRequest.get("tipo")) == null || (tipoRequest = jsonTipo.getAsString()) == null)
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		switch (tipoRequest)
		{
//			 Si el tipo es todos, devolver número de usuarios conectados
//			 Incluye registrados e invitados
			case "todos":
				Integer numUsuariosConectados = (Integer) request.getServletContext().getAttribute("usuarios.conectados");
				json.addProperty("cantidad", numUsuariosConectados);
				break;
				
//			Devuelve tabla de usuarios conectados en formato JSON
			case "registrados":
		
				mdlUsuariosConectados.getUsuariosConectados();
				
				while (mdlUsuariosConectados.getProximoUsuario())
				{
					jsonUsuario = new JsonObject();
					
					jsonUsuario.addProperty("uid", mdlUsuariosConectados.uid);
					jsonUsuario.addProperty("nombre", mdlUsuariosConectados.nombre);
					jsonUsuario.addProperty("pais", mdlUsuariosConectados.pais);
					
					jsonArrUsuarios.add(jsonUsuario);
				}
				
				json.add("usuarios", jsonArrUsuarios);
				
				break;
		}
		
		response.getWriter().write(json.toString());
	}

}
