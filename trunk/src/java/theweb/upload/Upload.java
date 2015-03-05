package theweb.upload;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Upload {
    String name;
    int size;
    String contentType;
    byte[] content;

    public InputStream getInputStream() {
        return new ByteArrayInputStream(getContent());
    }

    public byte[] getContent(){
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setContent(InputStream inputStream) {
        content = new byte[size];

        try {
            for (int i = 0; i < size; i ++)
                content[i] = (byte) inputStream.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
