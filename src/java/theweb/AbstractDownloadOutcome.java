package theweb;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractDownloadOutcome implements Outcome {
    
    public abstract String getContentType();
    public abstract int getSize();
    public abstract InputStream getInputStream();
    
    public boolean isCacheable() {
        return false;
    }
    
    public boolean isAttachment() {
        return true;
    }
    
    public String getFileName() {
        return null;
    }
    
    @Override
    public void process(Page page, HttpExchange exchange) throws Exception {
        exchange.setContentType(getContentType());

        if (!isCacheable()) {
            String userAgent = exchange.getRequestHeader("User-Agent");
            
            if (userAgent != null && !userAgent.contains("MSIE"))
                //IE 6.0 SP1 fails to save files with such header
                //http://support.microsoft.com/default.aspx?scid=kb;en-us;812935&Product=ie600
                exchange.addResponseHeader("Cache-Control", "no-cache"); 

            exchange.addResponseHeader("Pragma", "no-cache");
            exchange.addResponseHeader("Pragma-directive", "no-cache");
            exchange.addResponseHeader("Cache-Directive", "no-cache");
            exchange.addResponseHeader("Expires", "0");
            
            exchange.setContentLength(getSize());
        }

        String contentDisposition = isAttachment() ? "attachment" : "inline";
        if (getFileName() != null) contentDisposition += "; filename=" + getFileName();
        
        exchange.addResponseHeader("Content-Disposition", contentDisposition);
        
        
        byte[] buffer = new byte[1024];
        InputStream source = getInputStream();
        OutputStream target = exchange.getOutputStream();
        
        while (true) {
            int actuallyRead = source.read(buffer);

            target.write(buffer, 0, actuallyRead);
            
            if (actuallyRead < buffer.length) break;
        }
        
        target.close();
    }
}
