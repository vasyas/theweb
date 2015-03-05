package theweb;

import java.io.InputStream;
import java.io.OutputStream;

public interface DownloadResponse extends Response {
    String getContentType();
    int getSize();
    InputStream getInputStream();

    default boolean isAttachment() {
        return true;
    }
    default String getFileName() {
        return null;
    }

    default void send(Page page, HttpExchange exchange) throws Exception {
        exchange.setContentType(getContentType());

        exchange.addResponseHeader("Pragma-directive", "no-cache");
        exchange.addResponseHeader("Cache-Directive", "no-cache");
        exchange.addResponseHeader("Expires", "0");

        exchange.setContentLength(getSize());

        String contentDisposition = isAttachment() ? "attachment" : "inline";
        if (getFileName() != null) {
            String fileName = "";

            for (char ch : getFileName().toCharArray()) {
                if (ch < 128) fileName += ch;
                else fileName += "_";
            }

            contentDisposition += "; filename=" + fileName;
        }

        exchange.addResponseHeader("Content-Disposition", contentDisposition);

        OutputStream target = exchange.getOutputStream();

        if (!exchange.getRequestMethod().equals("HEAD")) {
            byte[] buffer = new byte[1024];
            InputStream source = getInputStream();

            while (true) {
                int actuallyRead = source.read(buffer);

                if (actuallyRead <= 0) break;

                target.write(buffer, 0, actuallyRead);

                if (actuallyRead < buffer.length) break;
            }
        }

        target.close();
    }
}
