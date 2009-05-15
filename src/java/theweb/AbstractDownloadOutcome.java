package theweb;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    
    @Override
    public void process(Page page, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());

        if (!isCacheable()) {
            String userAgent = request.getHeader("User-Agent");
            
            if (userAgent != null && !userAgent.contains("MSIE"))
                //IE 6.0 SP1 fails to save files with such header
                //http://support.microsoft.com/default.aspx?scid=kb;en-us;812935&Product=ie600
                response.setHeader("Cache-Control", "no-cache"); 

            response.setHeader("Pragma", "no-cache");
            response.setHeader("Pragma-directive", "no-cache");
            response.setHeader("Cache-Directive", "no-cache");
            response.setHeader("Expires", "0");
            
            response.setContentLength(getSize());
        }

        String contentDisposition = isAttachment() ? "attachment" : "inline";
        response.setHeader("Content-Disposition", contentDisposition);
        
        byte[] buffer = new byte[1024];
        InputStream source = getInputStream();
        OutputStream target = response.getOutputStream();
        
        while (true) {
            int actuallyRead = source.read(buffer);

            target.write(buffer, 0, actuallyRead);
            
            if (actuallyRead < buffer.length) break;
        }
        
        target.close();
    }
}
