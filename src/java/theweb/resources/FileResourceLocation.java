package theweb.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileResourceLocation implements ResourceLocation {
	private final File file;

	public FileResourceLocation(File file) {
		this.file = file;
	}

	public InputStream getInputStream() {
		try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
	}

	public String getName() {
		return file.getName();
	}

	public long lastModified() {
		return file.lastModified();
	}
	
	@Override
	public String toString() {
		return "file://" + file.getPath();
	}

    @Override
    public String getPath() {
        return file.getPath();
    }
}