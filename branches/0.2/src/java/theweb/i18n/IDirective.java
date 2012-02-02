package theweb.i18n;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

public class IDirective extends Directive {

    @Override
    public String getName() {
        return "i";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        if (node.jjtGetNumChildren() < 1) {
            rsvc.error("#i() error : invalid argument count");
            
            return true;
        }
        
        Object key = node.jjtGetChild(0).value(context);
        
        if (key == null) {
            rsvc.error("#i18n() error : null passed as key");
            
            return true;
        }
        
        if (node.jjtGetNumChildren() == 1) {
            writer.write(Resources.getText(key.toString()));
        } else {
            String[] params = new String[node.jjtGetNumChildren() - 1];
            
            for (int i = 1; i < node.jjtGetNumChildren(); i ++) {
                params[i - 1] = String.valueOf(node.jjtGetChild(i).value(context));
            }
            
            writer.write(Resources.getText(key.toString(), (Object[]) params));
        }
        
        return true;
    }
}
