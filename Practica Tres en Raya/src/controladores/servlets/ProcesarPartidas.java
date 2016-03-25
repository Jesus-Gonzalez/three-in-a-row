package controladores.servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

@WebServlet("/partidas/procesar")
public class ProcesarPartidas extends HttpServlet {
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
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		try
		{
			RecordarmeHelper hlpRecordarme = new RecordarmeHelper();
			hlpRecordarme.comprobarCookieRecordarme(request, response, session);
	
			
			Connection conexion = (Connection) session.getAttribute("conexion");
			SesionUsuario s = (SesionUsuario) session.getAttribute("sesion");
			
			JsonParser parser = new JsonParser();
			JsonObject json = new JsonObject(), jsonRequest = new JsonObject();
			JsonElement jsonTipo, jsonPid, jsonCasilla;
			String tipoRequest, pid;
			int casilla = -1;
			
			jsonRequest = parser.parse(new JsonReader(request.getReader())).getAsJsonObject();

			jsonCasilla = jsonRequest.get("casilla");
			
			if ( (jsonTipo = jsonRequest.get("tipo")) == null || (tipoRequest = jsonTipo.getAsString()) == null ||
				 (jsonPid = jsonRequest.get("pid")) == null || (pid = jsonPid.getAsString()) == null ||
				 (tipoRequest.equals("mover") && jsonCasilla == null))
			{
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
			if (tipoRequest.equals("mover"))
			{
				casilla = jsonCasilla.getAsInt();
			}
			
			HashMap<String, Partida> mapaPartidas = (HashMap<String,Partida>) getServletContext().getAttribute("partidas.activas");
			
			Partida partida = mapaPartidas.get(pid);

			if (partida == null || partida.estado == Partida.DESAFIADO)
			{
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			
			UsuarioJugando usrDesafiante = partida.jugadores[0], usrDesafiado = partida.jugadores[1];
			
			
			MUsuarios mdlUsuarios;
			
			switch (tipoRequest)
			{
				case "getDatos":
					
					JsonObject 	desafiante = new JsonObject(),
								desafiado = new JsonObject();
					
					if (partida.estado == Partida.JUGANDO && (Calendar.getInstance().getTimeInMillis() - partida.timeUltimoMovimiento) > 59000)
					{
						if (usrDesafiante.turno)
						{
							usrDesafiado.ganador = true;
							usrDesafiado.ganadasActual++;
							usrDesafiado.datosPartidas[Usuario.JUGADAS]++; // Aumentar partidas jugadas
							usrDesafiado.datosPartidas[Usuario.GANADAS]++; // Aumentar partidas jugadas
							
							usrDesafiante.perdidasActual++;
							usrDesafiante.datosPartidas[Usuario.JUGADAS]++;
							usrDesafiante.datosPartidas[Usuario.PERDIDAS]++;									
							
							mdlUsuarios = new MUsuarios(conexion, usrDesafiado);
							mdlUsuarios.actualizarDatosPartidas(usrDesafiado.uid, usrDesafiado.datosPartidas);
							
							mdlUsuarios = new MUsuarios(conexion, usrDesafiante);
							mdlUsuarios.actualizarDatosPartidas(usrDesafiante.uid, usrDesafiante.datosPartidas);
							
						} else if (usrDesafiado.turno) {
							
							usrDesafiante.ganador = true;
							usrDesafiante.ganadasActual++;
							usrDesafiante.datosPartidas[Usuario.JUGADAS]++; // Aumentar partidas jugadas
							usrDesafiante.datosPartidas[Usuario.GANADAS]++; // Aumentar partidas jugadas
							
							usrDesafiado.perdidasActual++;
							usrDesafiado.datosPartidas[Usuario.JUGADAS]++;
							usrDesafiado.datosPartidas[Usuario.PERDIDAS]++;
							
							mdlUsuarios = new MUsuarios(conexion, usrDesafiante);
							mdlUsuarios.actualizarDatosPartidas(usrDesafiante.uid, usrDesafiante.datosPartidas);
							
							mdlUsuarios = new MUsuarios(conexion, usrDesafiado);
							mdlUsuarios.actualizarDatosPartidas(usrDesafiado.uid, usrDesafiado.datosPartidas);
							
						}
						
						usrDesafiante.turno = false;
						usrDesafiado.turno = false;
					
						partida.estado = Partida.TERMINADA;
						partida.resultado = Partida.GANADOR;
						
						json.addProperty("estado", partida.estado);
						
					} else
					
						json.addProperty("estado", partida.estado);
					
					
					desafiante.addProperty("uid", usrDesafiante.uid);
					desafiante.addProperty("nombre", usrDesafiante.nombre);
					desafiante.addProperty("turno", usrDesafiante.turno);
					desafiante.addProperty("pais", usrDesafiante.pais);
					desafiante.addProperty("ganador", usrDesafiante.ganador);
					desafiante.addProperty("ganadas", usrDesafiante.ganadasActual);
					desafiante.addProperty("perdidas", usrDesafiante.perdidasActual);
					desafiante.addProperty("empatadas", usrDesafiante.empatesActual);
					desafiante.addProperty("jugadas", (usrDesafiante.ganadasActual + usrDesafiante.perdidasActual + usrDesafiante.empatesActual));
					json.add("desafiante", desafiante);

					desafiado.addProperty("uid", usrDesafiado.uid);
					desafiado.addProperty("nombre", usrDesafiado.nombre);
					desafiado.addProperty("turno", usrDesafiado.turno);
					desafiado.addProperty("pais", usrDesafiado.pais);
					desafiado.addProperty("ganador", usrDesafiado.ganador);
					desafiado.addProperty("ganadas", usrDesafiado.ganadasActual);
					desafiado.addProperty("perdidas", usrDesafiado.perdidasActual);
					desafiado.addProperty("empatadas", usrDesafiado.empatesActual);
					desafiado.addProperty("jugadas", (usrDesafiado.ganadasActual + usrDesafiado.perdidasActual + usrDesafiado.empatesActual));
					json.add("desafiado", desafiado);					
					
					json.addProperty("resultado", partida.resultado);
					json.addProperty("partidasJugadas", partida.partidasJugadas);
					
					String jsonStrMovimientos = new Gson().toJson(partida.movimientos);
					JsonArray jsonMovimientos = parser.parse(jsonStrMovimientos).getAsJsonArray();
					json.add("movimientos", jsonMovimientos);
					
					json.addProperty("timeUltimoMovimiento", partida.timeUltimoMovimiento);
					json.addProperty("serverTime", Calendar.getInstance().getTimeInMillis());
					
					break;
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
				case "mover":
					
					int mid = (usrDesafiante.turno) ? -1 : 1; // Movimiento ID
					
					if ((usrDesafiante.turno && usrDesafiante.uid == s.usuario.uid && usrDesafiante.contrasena.equals(s.usuario.contrasena)) ||
						(usrDesafiado.turno  && usrDesafiado.uid == s.usuario.uid && usrDesafiado.contrasena.equals(s.usuario.contrasena)))
					{
						int fila = -1, col = -1;
						
						// Get fila
						if (casilla <= 3)
						{
							fila = 0;

						} else if (casilla > 3 && casilla <= 6) {

							fila = 1;

						} else if (casilla > 6 && casilla <= 9) {

							fila = 2;

						}
						
						// Get col
						if (casilla == 1 || casilla == 4 || casilla == 7)
						{
							col = 0;
							
						} else if (casilla == 2 || casilla == 5 || casilla == 8) {
							
							col = 1;
							
						} else if (casilla == 3 || casilla == 6 || casilla == 9) {
							
							col = 2;
						}
						
						if (fila != -1 && col != -1 && partida.movimientos[fila][col] == 0)
						{
							partida.movimientos[fila][col] = mid;
							
							// Comprobar si hay 3 en raya
							int usrHorizontales = 0,
								usrVerticales = 0,
								usrDiagonales = 0;
							
							boolean hayTresEnRaya = false,
									quedanCasillas = false;
							
							// Comprobar tres en raya horizontales y verticales
							// Comprobar además si quedan casillas libres
							for (int i=0; i < 3; i++)
							{
								usrHorizontales = usrVerticales = 0;
								
								for (int j=0; j < 3; j++)
								{
									if (partida.movimientos[i][j] == 0) {
										
										quedanCasillas = true;
									}
									
									if (partida.movimientos[i][j] == mid)
									{
										usrHorizontales++;
									}
									
									if (partida.movimientos[j][i] == mid) {
										
										usrVerticales++;
									}
								}

								// Si hay tres en raya, salir del bucle for
								if (usrHorizontales == 3 || usrVerticales == 3)
								{
									hayTresEnRaya = true;
									break;
								}
							}
							
							// Si no hay tres en raya horizontal ni vertical, comprobar diagonales
							if (! hayTresEnRaya)
							{
//								Comprobar tres en raya diagonal de 'top left' a 'bottom right'
								for (int i=0, j=0; i < 3 && j < 3; i++)
								{
									if (partida.movimientos[i][j] == mid)
									{
										 usrDiagonales++;
									}
									
									j++;
								}
								
								hayTresEnRaya = (usrDiagonales == 3) ? true : false;								
							}
							
							if (! hayTresEnRaya )
							{
								usrDiagonales = 0;
								
//								Comprobar tres en raya diagonal de 'bottom left' a 'top right'
								for (int i=2, j=0; i >= 0 && j <= 2; i--)
								{
									if (partida.movimientos[i][j] == mid)
									{
										 usrDiagonales++;
									}
									
									j++;
								}
								
								hayTresEnRaya = (usrDiagonales == 3) ? true : false;
							}

							if (hayTresEnRaya)
							{
								partida.estado = Partida.FIN;
								partida.resultado = Partida.GANADOR;
								partida.partidasJugadas++;
								
								if (usrDesafiante.turno)
								{
									usrDesafiante.ganador = true;
									usrDesafiante.ganadasActual++;
									usrDesafiante.datosPartidas[Usuario.JUGADAS]++; // Aumentar partidas jugadas
									usrDesafiante.datosPartidas[Usuario.GANADAS]++; // Aumentar partidas jugadas
									
									usrDesafiado.perdidasActual++;
									usrDesafiado.datosPartidas[Usuario.JUGADAS]++;
									usrDesafiado.datosPartidas[Usuario.PERDIDAS]++;
									
									mdlUsuarios = new MUsuarios(conexion, usrDesafiante);
									mdlUsuarios.actualizarDatosPartidas(usrDesafiante.uid, usrDesafiante.datosPartidas);
									
									mdlUsuarios = new MUsuarios(conexion, usrDesafiado);
									mdlUsuarios.actualizarDatosPartidas(usrDesafiado.uid, usrDesafiado.datosPartidas);
									
								} else {
									
									usrDesafiado.ganador = true;
									usrDesafiado.ganadasActual++;
									usrDesafiado.datosPartidas[Usuario.JUGADAS]++; // Aumentar partidas jugadas
									usrDesafiado.datosPartidas[Usuario.GANADAS]++; // Aumentar partidas jugadas
									
									usrDesafiante.perdidasActual++;
									usrDesafiante.datosPartidas[Usuario.JUGADAS]++;
									usrDesafiante.datosPartidas[Usuario.PERDIDAS]++;									
									
									mdlUsuarios = new MUsuarios(conexion, usrDesafiado);
									mdlUsuarios.actualizarDatosPartidas(usrDesafiado.uid, usrDesafiado.datosPartidas);
									
									mdlUsuarios = new MUsuarios(conexion, usrDesafiante);
									mdlUsuarios.actualizarDatosPartidas(usrDesafiante.uid, usrDesafiante.datosPartidas);
									
								}
								
								usrDesafiado.turno = false;
								usrDesafiante.turno = false;

								json.addProperty("ganador", true);
							}
								else if (! quedanCasillas)
							{
									partida.estado = Partida.FIN;
									partida.resultado = Partida.EMPATE;
									partida.partidasJugadas++;
									
									usrDesafiante.empatesActual++;
									usrDesafiado.empatesActual++;
									
									usrDesafiante.ganador = true;
									usrDesafiante.empatesActual++;
									usrDesafiante.datosPartidas[Usuario.JUGADAS]++; // Aumentar partidas jugadas
									usrDesafiante.datosPartidas[Usuario.EMPATADAS]++; // Aumentar partidas jugadas
																		
									usrDesafiado.empatesActual++;
									usrDesafiado.datosPartidas[Usuario.JUGADAS]++;
									usrDesafiado.datosPartidas[Usuario.EMPATADAS]++;

									mdlUsuarios = new MUsuarios(usrDesafiante);
									mdlUsuarios.actualizarDatosPartidas(usrDesafiante.uid, usrDesafiante.datosPartidas);
									
									mdlUsuarios = new MUsuarios(usrDesafiado);
									mdlUsuarios.actualizarDatosPartidas(usrDesafiado.uid, usrDesafiante.datosPartidas);
									
									json.addProperty("empate", true);
									
							} else {
							
								if (mid == 1)
								{
									usrDesafiante.turno = true;
									usrDesafiado.turno = false;
									
								} else {
									
									usrDesafiado.turno = true;
									usrDesafiante.turno = false;
								}
								
								partida.timeUltimoMovimiento = Calendar.getInstance().getTimeInMillis();
								
								json.addProperty("exitoMovimiento", true);
							}
							
						} else {
						
							response.sendError(HttpServletResponse.SC_BAD_REQUEST);
							return;
						}
						
						
					} else {
						
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
						return;
						
					}

					break;
					
				
					
					
					
					
					
					
					
					
					
					
					
				case "reiniciar":

					if (partida.estado != Partida.FIN)
					{
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
					
						else if ((usrDesafiante.uid == s.usuario.uid && usrDesafiante.contrasena.equals(s.usuario.contrasena)) ||
								 (usrDesafiado.uid == s.usuario.uid && usrDesafiado.contrasena.equals(s.usuario.contrasena)))
					{
						Random r = new Random();
						
						partida = mapaPartidas.get(pid);
						
						for (int i=0; i < partida.movimientos.length; i++)
						{
							for (int j=0; j < partida.movimientos[i].length; j++)
							{
								partida.movimientos[i][j] = 0;
							}
						}
						
						partida.partidasJugadas++;
						usrDesafiado.ganador = false;
						usrDesafiante.ganador = false;
						partida.estado = Partida.JUGANDO;
						partida.resultado = Partida.EN_DESARROLLO;
	
						partida.timeUltimoMovimiento = Calendar.getInstance().getTimeInMillis();
						
						partida.jugadores[r.nextInt(2)].turno = true;
					
					} else {
						
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
						return;
					}
					
					break;
					
					
					
				case "terminar":
					
					if (partida.estado != Partida.FIN)
					{
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
					
						else if ((usrDesafiante.uid == s.usuario.uid && usrDesafiante.contrasena.equals(s.usuario.contrasena)) ||
								 (usrDesafiado.uid == s.usuario.uid && usrDesafiado.contrasena.equals(s.usuario.contrasena)))
					{

							if (partida.estado == Partida.TERMINADA)
							{
								response.sendError(HttpServletResponse.SC_BAD_REQUEST);
								return;
							}
							
							MPartidas mdlPartidas = new MPartidas(conexion);

							mdlPartidas.borrarPartidaByPid(pid);

							partida.estado = Partida.TERMINADA;

							json.addProperty("success", true);
							
					} else {
						
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
						return;
					}
					
					break;
					
					
					
					
					
					
				
				default:
					
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
					
					break;
			}

			out.write(json.toString());
			
			return;
			
		} catch (JsonParseException x) {
			
			x.printStackTrace();
			System.exit(-120000);
		}
		
		JsonObject jsonError = new JsonObject();
		jsonError.addProperty("error", true);
		
		out.write(jsonError.toString());
	}

}
