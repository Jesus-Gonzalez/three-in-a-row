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

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import helpers.RecordarmeHelper;
import modelos.MPartidas;
import modelos.MUsuarios;
import modelos.Partida;
import modelos.SesionUsuario;
import modelos.Usuario;
import modelos.UsuarioJugando;
import utils.Cifrado;

@WebServlet("/partidas/crear")
public class CrearPartida extends HttpServlet {
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

		response.setContentType("application/json");
		
		Connection conexion = (Connection) session.getAttribute("conexion");
		
		SesionUsuario s = (SesionUsuario) session.getAttribute("sesion");
		Usuario usuario = s.usuario;
		
		HashMap<String, Partida> mapaPartidas = (HashMap<String, Partida>) getServletContext().getAttribute("partidas.activas");
		
		Cifrado cifrado = new Cifrado();
		Random r = new Random();
		
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(new JsonReader(request.getReader())).getAsJsonObject();
		
		try
		{	
			if (usuario != null)
			{
				MPartidas mdlPartidas = new MPartidas(conexion);
				
				MUsuarios mdlUsuarios = new MUsuarios(conexion);
				long uid = json.get("uid").getAsLong();
				
				if (usuario.uid == uid)
				{
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
				
				mdlUsuarios.getUsuarioByUid(uid);
				
				if (mdlUsuarios.getProximoUsuario())
				{
					String pid = cifrado.hash("SHA-1", usuario.nombre + r.nextInt(Integer.MAX_VALUE));
					
					Partida partida = new Partida();
					partida.pid = pid;
					partida.estado = Partida.DESAFIADO;

					UsuarioJugando 	desafiante = new UsuarioJugando(s.usuario),
									desafiado = new UsuarioJugando(mdlUsuarios);
					
					partida.jugadores[0] = desafiante;
					partida.jugadores[1] = desafiado;
					partida.jugadores[0].ganador = false;
					partida.jugadores[1].ganador = false;
					
					partida.partidasJugadas = 0;
					
					partida.fechaCreacion = Calendar.getInstance().getTimeInMillis();
					
					for (int i=0; i < partida.movimientos.length; i++)
					{
						for (int j=0; j < partida.movimientos[i].length; j++)
						{
							partida.movimientos[i][j] = 0;
						}
					}
					
					mdlPartidas.creaPartida(pid, desafiante.uid, desafiado.uid);
					
					mapaPartidas.put(pid, partida);
					
					// Obtenemos los datos para la partida y la creamos como un objeto JSON
					JsonObject jsonPartida = new JsonObject(),
							   jsonJugador;
					
					jsonPartida.addProperty("pid", partida.pid);
					
					jsonJugador = new JsonObject();
					jsonJugador.addProperty("uid", partida.jugadores[0].uid);
					jsonJugador.addProperty("nombre", partida.jugadores[0].nombre);
					jsonPartida.add("desafiante", jsonJugador);
					
					jsonJugador = new JsonObject();
					jsonJugador.addProperty("uid", partida.jugadores[1].uid);
					jsonJugador.addProperty("nombre", partida.jugadores[1].nombre);
					jsonPartida.add("desafiado", jsonJugador);
					
					response.getWriter().write(jsonPartida.toString());
					
				} // No existe el usuario
				
			} // El usuario no está identificado en el sistema
				
			
		} catch (ClassCastException x) {
			
			x.printStackTrace();
//			No salimos de la aplicación
//			System.exit
		} catch (JsonParseException x) {
			
			x.printStackTrace();
			// No salimos de la aplicación
//			System.exit
		}
	}

}
