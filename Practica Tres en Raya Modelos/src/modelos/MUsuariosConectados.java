package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MUsuariosConectados
{
	private Connection conexion;
	private ResultSet rs;
	
	public long uid;
	public String nombre, pais;
	
	public MUsuariosConectados(Connection conexion)
	{
		this.conexion = conexion;
	}

	public boolean getProximoUsuario()
	{
		try
		{
			if (rs.next())
			{
				uid = rs.getLong("uid");
				nombre = rs.getString("nombre");
				pais = rs.getString("pais");
				
				return true;
				
			} else {
				
				return false;
			}
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MUsuariosConectados:getProximoUsuario(void)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-10000);
		}
		
		return false;
	}
	
	public boolean estaUsuarioConectado(long uid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.usuarios_conectados WHERE uid = ?");
			ps.setLong(1, uid);
			
			return ps.executeQuery().next();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MUsuariosConectados:estaUsuarioConectado(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-10001);
		}
		
		return false;
	}
	
	public void crearUsuarioConectado(long uid, String nombre, String pais)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("INSERT INTO tresenraya.usuarios_conectados (uid, nombre, pais) VALUES(?, ?, ?)");
			ps.setLong(1, uid);
			ps.setString(2, nombre);
			ps.setString(3, pais);
			ps.executeUpdate();
			
		} catch (SQLException x) {
	
			System.err.println("Error SQL -> MUsuariosConectados:crearUsuarioConectado(long, String, String)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-10002);
		}
	}
	
	public void eliminarUsuarioConectadoByUid(long uid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("DELETE FROM tresenraya.usuarios_conectados WHERE uid = ?");
			ps.setLong(1, uid);
			ps.executeUpdate();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MUsuariosConectados:eliminarUsuarioConectadoByUid(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-10003);
		}
	}
	
	public void getUsuariosConectados()
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.usuarios_conectados");
			
			rs = ps.executeQuery();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MUsuariosConectados:getUsuariosConectados(void)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-10004);
		}
	}
	
	public int countUsuariosConectados()
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT COUNT(*) AS total FROM tresenraya.usuarios_conectados");
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				return rs.getInt("total");	
			}
			
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MUsuariosConectados:countUsuariosConectados(void)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-10005);
		}
		
		return 0;
	}
	
	public void vaciarTablaUsuariosConectados()
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("TRUNCATE TABLE tresenraya.usuarios_conectados");
			ps.executeUpdate();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MUsuariosConectados:vaciarTablaUsuariosConectados(void)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-10006);
		}
	}
}
