package theweb.validation;

import java.util.regex.Pattern;

import theweb.Messages;

public class EmailValidator extends ParametrizedValidator<Email> implements Validator<String> {
	public void validate(Object object, String property) {
        if (property == null) return;
        if (property.isEmpty()) return;
        
		if (!isValidEmail(property)) Messages.error(annotation.msg());
	}
	
    //RFC 2822 token definitions for valid email - only used together to form a java Pattern object:
    private static final String sp = "\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~";
    private static final String atext = "[a-zA-Z0-9" + sp + "]";
    private static final String atom = atext + "+"; //one or more atext chars
    private static final String dotAtom = "\\." + atom; 
    private static final String localPart = atom + "(" + dotAtom + ")*"; //one atom followed by 0 or more dotAtoms.

    //RFC 1035 tokens for domain names:
    private static final String letter = "[a-zA-Z]";
    private static final String letDig = "[a-zA-Z0-9]";
    private static final String letDigHyp = "[a-zA-Z0-9-]";
    private static final String rfcLabel = letDig + "(" + letDigHyp + "{0,61}" + letDig + ")?";
    private static final String domain = rfcLabel + "(\\." + rfcLabel + ")*\\." + letter + "{2,6}";

    //Combined together, these form the allowed email regexp allowed by RFC 2822:
    private static final String addrSpec = "^" + localPart + "@" + domain + "$";

    //now compile it:
    private static final Pattern EMAIL_PATTERN = Pattern.compile( addrSpec );
    
    public static boolean isValidEmail(String s) {
        return s != null && EMAIL_PATTERN.matcher(s).matches();
    }
}