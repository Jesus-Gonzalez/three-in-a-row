package utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Correo
{
	private Message mensaje;

	private String correo;
	
	public Correo(String correo)
	{
		this.correo = correo;
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.mail.server.github");
		props.put("mail.smtp.port", "587");

		Session sesion = Session.getInstance(props,
							  new javax.mail.Authenticator() {
								protected PasswordAuthentication getPasswordAuthentication() {
									return new PasswordAuthentication("user",
																	  "password");
								}
							  });
		
		mensaje = new MimeMessage(sesion);
	}
	
	public void enviarCorreoHtml(String html, String from, String titulo)
	{
		try
		{
			mensaje.setFrom(new InternetAddress(from));
			mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(correo));
			mensaje.setSubject(titulo);
			mensaje.setContent(html, "text/html; charset=utf-8");
			
			Transport.send(mensaje);
			
		} catch (MessagingException x) {
			
			System.err.println("Error Envío Email -> Correo:enviarCorreoHtml(String, String, String)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
		
			System.exit(-750);
			
		}
	}
	
	public void enviarCorreoActivacion(String url, String clave, long aid)
	{	
		String html = 	"<h1>Tres en Raya</h1>" +
						"<h2>Activación de Cuenta</h2>" +
						"<p>Ha recibido este correo electrónico para activar su cuenta en 'Tres en Raya'</p>" +
						"<p>Una vez acceda al enlace siguiente, podrá empezar a desafiar a otros usuarios.</p>" +
						"<p><a href='" + url + "/activacion.jsp?id=" + aid + "&clave=" + clave + "'>Click aquí para activar su cuenta</a></p>" +
						"<p>En caso de no disponer de un cliente de correo que soporte formato HTML, copie y pegue la siguiente dirección en la barra de navegación de su navegador web:</p>" +
						"<p>" + url + "/activacion.jsp?id=" + aid + "&clave=" + clave + "</p>" +
						"<br>" +
						"<p><strong>Tres en Raya</strong></p>";
	
		enviarCorreoHtml(html, "activaciones@tresenraya", "Tres en Raya - Activar Cuenta");
		
	}
	
	public void enviarCorreoAviso()
	{	
		// TODO URL
		String html = 	"<h1>Tres en Raya</h1>" +
						"<h2>Activación de Cuenta</h2>" +
						"<p>Le recordamos que debe activar su cuenta en el sitio.</p>" +
						"<p>Dentro de 3 días, si no ha activado su cuenta, procederemos a eliminarla por completo del sitio.</p>" +
						"<p>Nota: <i>Si no ha recibido la clave de activación, puede solicitar una clave nueva desde el sitio web.</i></p>" +						
						"<br>" +
						"<p><strong>Tres en Raya</strong></p>";
	
		enviarCorreoHtml(html, "activaciones@tresenraya", "Tres en Raya - Aviso de Activación de Cuenta");
	}
	
	public void enviarCorreoCuentaEliminada()
	{
		String html = 	"<h1>Tres en Raya</h1>" +
				"<h2>Cuenta Eliminada</h2>" +
				"<p>Su cuenta ha sido eliminada del sitio.</p>" +
				"<p>Si lo desea, puede volver a registrarse, pero recuerde activar su cuenta completamente.</p>" +						
				"<br>" +
				"<p><strong>Tres en Raya</strong></p>";

		enviarCorreoHtml(html, "activaciones@tresenraya", "Tres en Raya - Aviso de Activación de Cuenta");
	}
	
	public void enviarNombreDeUsuario(String nombre)
	{
		String html = 	"<h1>Tres en Raya</h1>" +
				"<h2>Recordar Nombre</h2>" +
				"<p>Usted ha solicitado recuperar su nombre.</p>" +
				"<p>Su nombre de usuario en el sitio es: <strong>" + nombre + "</strong>.</p>" +						
				"<br>" +
				"<p><strong>Tres en Raya</strong></p>";

		enviarCorreoHtml(html, "activaciones@tresenraya", "Tres en Raya - Recuperación de Nombre Usuario");
	}
	
	public void enviarContrasena(String contrasena)
	{
		String html = 	"<h1>Tres en Raya</h1>" +
				"<h2>Recordar Contraseña</h2>" +
				"<p>Usted ha solicitado recuperar su contraseña.</p>" +
				"<p>Su contraseña en el sitio es: <strong>" + contrasena + "</strong>.</p>" +						
				"<br>" +
				"<p><strong>Tres en Raya</strong></p>";

		enviarCorreoHtml(html, "activaciones@tresenraya", "Tres en Raya - Recuperación de Contraseña");
	}
}
