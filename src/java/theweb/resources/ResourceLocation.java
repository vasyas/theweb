package theweb.resources;

import java.io.InputStream;

public interface ResourceLocation {
	String getName();
	String getPath();
	InputStream getInputStream();
	long lastModified();
}
