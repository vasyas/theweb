package theweb.i18n;

import java.io.InputStream;

import javax.servlet.ServletContext;

public class ServletContextResourceLocation implements ResourceLocation {

	private final ServletContext servletContext;
	private final String name;

	public ServletContextResourceLocation(ServletContext servletContext,
			String name) {
		this.servletContext = servletContext;
		this.name = name;
	}
	
	public InputStream getInputStream() {
		return servletContext.getResourceAsStream(name);
	}

	public String getName() {
		return "Servlet:" + name;
	}

	public long lastModified() {
		// no reloading for servlet context resources
		return 0;
	}

}
