package theweb.validation;

import junit.framework.TestCase;
import theweb.Page;

public class FormValidationTest extends TestCase {
    class Base {
        @NotBlank("base")
        public String baseField;
    }
    
    class Derived extends Base {
        @NotBlank("derived")
        public String field;
    }
    
    FormValidation validation = new FormValidation();
    
    public void testValidateFields() throws Exception {
        validation.run(new Base());
        
        assertEquals(1, validation.getMessages().size());
    }
    
    public void testValidateSuperclassFields() throws Exception {
        validation.run(new Derived());
        
        assertEquals(2, validation.getMessages().size());
    }
    
    public void testValidateNotAccessibleFields() throws Exception {
        validation.run(new Page("/") {
            @SuppressWarnings("unused")
            @NotBlank
            String b;
        });
        
        assertEquals(1, validation.getMessages().size());
    }
}
