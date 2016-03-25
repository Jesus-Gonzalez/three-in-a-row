package modelos;

public class Partida
{	
	public static final
					int DESAFIADO = 0,
						JUGANDO = 1,
						FIN = 2,
						TERMINADA = 3,
							
						EN_DESARROLLO = -1,
						EMPATE = 0,
						GANADOR = 1;

	public
		String pid;
	
	public
		int partidasJugadas,
			estado,
			resultado,
			invitados;
	
	public
		UsuarioJugando[] jugadores = new UsuarioJugando[2];

	// Se identificará cada movimiento con el UID del usuario
	public
		long[][] movimientos = new long[3][3];
	
	public
		long timeUltimoMovimiento,
			 fechaCreacion;
	
}
