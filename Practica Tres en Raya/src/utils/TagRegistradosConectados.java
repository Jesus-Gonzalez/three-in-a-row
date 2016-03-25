package utils;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import modelos.MUsuariosConectados;

public class TagRegistradosConectados
extends SimpleTagSupport
{
	
	@Override
	public void doTag()
	throws JspException, IOException
	{
		JspContext context = getJspContext();
		JspWriter out = context.getOut();
		Connection conexion = (Connection) context.getAttribute("conexion", PageContext.SESSION_SCOPE);

//		Una línea
//		out.println( new MUsuariosConectados(conexion).countUsuariosConectados() );
		
		MUsuariosConectados mdlUsuariosConectados = new MUsuariosConectados(conexion);

		out.println( mdlUsuariosConectados.countUsuariosConectados() );

	}

}
