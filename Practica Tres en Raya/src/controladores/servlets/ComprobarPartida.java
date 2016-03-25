package controladores.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

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

import helpers.RecordarmeHelper;
import modelos.MPartidas;
import modelos.Partida;
import modelos.SesionUsuario;

@WebServlet("/partidas/comprobar")
public class ComprobarPartida extends HttpServlet {
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
		hlpRecordarme.comprobarCookieRecordarme(request, response, sesion);
		
		HashMap<String, Partida> mapaPartidas = (HashMap<String, Partida>) getServletContext().getAttribute("partidas.activas");
		
		Connection conexion = (Connection) sesion.getAttribute("conexion");
		SesionUsuario s = (SesionUsuario) sesion.getAttribute("sesion");
		
		MPartidas mdlPartidas = new MPartidas(conexion);
		
		JsonParser parser = new JsonParser();
		JsonObject 	inputJSON = parser.parse(new JsonReader(request.getReader())).getAsJsonObject(),
				 	jsonPartida,
				 	jsonJugador;
		
		JsonArray	jsonPartidas = new JsonArray();
		
		Partida partida;
		
		JsonElement jsonTipo;
		String tipo;
		
		if ((jsonTipo = inputJSON.get("tipo")) == null || (tipo = jsonTipo.getAsString()) == null)
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		switch (tipo)
		{
			// Todas las partidas
			case "todas":
				
				mdlPartidas.buscarPartidas();
				
				break;
				
			// Desafios para el usuario en concreto
			case "desafios":
	
//				TODO Si está conectado no debería ni de comprobarse si el usuario en sesión es nulo.
				if (s.estado == SesionUsuario.CONECTADO || s.usuario == null)
				{
					return;
				}
			
				// Buscamos desafíos para el usuario actual en sesión
				mdlPartidas.buscarDesafiosByUid(s.usuario.uid);
				
				break;
				
			case "desafios.rechazados":
				
//				TODO Si está conectado no debería ni de comprobarse si el usuario en sesión es nulo.
				if (s.estado == SesionUsuario.CONECTADO || s.usuario == null)
				{
					return;
				}
				
				mdlPartidas.comprobarMisDesafiosRechazados(s.usuario.uid);
				
				break;
				
			case "desafios.aceptados":
				
//				TODO Si está conectado no debería ni de comprobarse si el usuario en sesión es nulo.
				if (s.estado == SesionUsuario.CONECTADO || s.usuario == null)
				{
					return;
				}
				
				mdlPartidas.comprobarMisDesafiosAceptados(s.usuario.uid);				
				
				break;
				
			case "desafios.enviados":
				
				if (s.estado == SesionUsuario.CONECTADO || s.usuario == null)
				{
					return;
				}
				
				mdlPartidas.comprobarMisDesafiosEnviados(s.usuario.uid);
				
				break;
				
			case "partidas.activas.usuario":
				
				if (s.estado == SesionUsuario.CONECTADO || s.usuario == null)
				{
					return;
				}
				
				mdlPartidas.getMisPartidasActivas(s.usuario.uid);
				
				break;
				
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
		}
				

//		Una vez se realiza la consulta adecuada
		while (mdlPartidas.getProximaPartida())
		{
			// Obtenemos la partida del mapa
			partida = mapaPartidas.get(mdlPartidas.pid);
			
			// Obtenemos los datos para la partida y la creamos como un objeto JSON
			jsonPartida = new JsonObject();
			
			jsonPartida.addProperty("pid", partida.pid);
			
			jsonJugador = new JsonObject();
			jsonJugador.addProperty("uid", partida.jugadores[0].uid);
			jsonJugador.addProperty("nombre", partida.jugadores[0].nombre);
			jsonPartida.add("desafiante", jsonJugador);
			
			jsonJugador = new JsonObject();
			jsonJugador.addProperty("uid", partida.jugadores[1].uid);
			jsonJugador.addProperty("nombre", partida.jugadores[1].nombre);
			jsonPartida.add("desafiado", jsonJugador);
			
			// Añadir la partida en formato json al array
			jsonPartidas.add(jsonPartida);
		}
		
		response.getWriter().write(jsonPartidas.toString());
	}
}
