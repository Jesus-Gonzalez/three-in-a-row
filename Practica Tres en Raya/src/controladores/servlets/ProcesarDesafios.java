package controladores.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import helpers.RecordarmeHelper;
import modelos.MPartidas;
import modelos.Partida;
import modelos.SesionUsuario;

/**
 * Servlet implementation class ProcesarDesafios
 */
@WebServlet("/desafios/procesar")
public class ProcesarDesafios extends HttpServlet {
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
		
		RecordarmeHelper hlpRecordarme = new RecordarmeHelper();
		hlpRecordarme.comprobarCookieRecordarme(request, response, session);

		Connection conexion = (Connection) session.getAttribute("conexion");
		SesionUsuario s = (SesionUsuario) session.getAttribute("sesion");
		
		JsonParser parser = new JsonParser();
		JsonObject jsonRequest = new JsonObject();
		JsonElement jsonTipo, jsonPid;
		String tipoRequest, pid;

		jsonRequest = parser.parse(new JsonReader(request.getReader())).getAsJsonObject();
		
		if ( (jsonTipo = jsonRequest.get("tipo")) == null || (tipoRequest = jsonTipo.getAsString()) == null ||
			 (jsonPid = jsonRequest.get("pid")) == null || (pid = jsonPid.getAsString()) == null)
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		if (s.estado == SesionUsuario.CONECTADO || s.usuario == null)
		{
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		HashMap<String, Partida> mapaPartidas = (HashMap<String, Partida>) getServletContext().getAttribute("partidas.activas");
		MPartidas mdlPartidas = new MPartidas(conexion);
		Partida partida;
		
		switch (tipoRequest)
		{
			case "aceptar":
				
				mdlPartidas.aceptarDesafio(pid);
				partida = mapaPartidas.get(pid);
				
				partida.estado = Partida.JUGANDO;
				partida.resultado = Partida.EN_DESARROLLO;
				partida.partidasJugadas = 0;

				Random r = new Random();
				
//				1º método para elegir jugador con turno inicial
//				Se calcula si el número entre 0 y Integer.MAX_VALUE es par o impar
				if (r.nextInt(Integer.MAX_VALUE) % 2 == 0)
				{
					partida.jugadores[0].turno = true;

				} else {
					
					partida.jugadores[1].turno = true;
				}
				
//				2º metodo elegir jugador aleatorio
//				partida.jugadores[r.nextInt(2)].turno = true;
				
				partida.timeUltimoMovimiento = Calendar.getInstance().getTimeInMillis();
				
				break;
				
				
				
				
				
			case "rechazar":
				
				mdlPartidas.rechazarDesafio(pid);
				
				break;
				
				
				
				
				
				
			case "eliminar":
				
				mdlPartidas.borrarPartidaByPid(pid);
				
				break;
				
				
				
				
				
				
				
				
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
		}
		
		JsonObject jsonSuccess = new JsonObject();
		jsonSuccess.addProperty("success", true);
		response.getWriter().write(jsonSuccess.toString());
	}

}
