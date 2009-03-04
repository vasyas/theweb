package theweb.i18n;

import java.io.InputStream;

public interface ResourceLocation {
	public String getName();
	public InputStream getInputStream();
	public long lastModified();
}
