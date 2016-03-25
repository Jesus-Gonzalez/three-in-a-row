package utils;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TagUsuariosConectados
extends SimpleTagSupport
{
	@Override
	public void doTag()
	throws JspException, IOException
	{
		JspContext context = getJspContext();
		JspWriter out = context.getOut();
		Integer numUsuariosConectados = (Integer) context.getAttribute("usuarios.conectados", PageContext.APPLICATION_SCOPE);
		
		if (numUsuariosConectados != null)
		{
			out.println(numUsuariosConectados);
		}
	}
}
