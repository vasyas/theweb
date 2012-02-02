package theweb.validation;

import theweb.Messages;

public class LoginValidator extends ParametrizedValidator<Login> implements Validator<String> {
	public void validate(Object object, String property) {
	    if (property == null) return;
	    if (property.isEmpty()) return;
	    
		if (!isValidLogin(property)) Messages.error(annotation.msg());
	}
	
    public static boolean isValidLogin(String login) {
        for(int i = 0; i < login.length(); i++) {
            char ch = login.charAt(i);

            if(Character.isWhitespace(ch))
                return false;
            if(ch >= 'a' && ch <= 'z')
                continue;
            if(ch >= 'A' && ch <= 'Z')
                continue;
            if(ch >= '0' && ch <= '9')
                continue;
            if(ch == '_' || ch == '.')
                continue;
            return false;
        }

        return true;
    }
}
