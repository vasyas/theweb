package theweb.validation;

import junit.framework.TestCase;
import theweb.Messages;

public class ValidationTest extends TestCase {
    class Base {
        @NotBlank("base")
        public String baseField;
    }
    
    class Derived extends Base {
        @NotBlank("derived")
        public String field;
    }
    
    @Override
    protected void setUp() throws Exception {
        new Messages().set();
    }
    
    public void testValidateFields() throws Exception {
        Validation.validate(new Base());
        
        assertEquals(1, Messages.get().getErrors().size());
    }
    
    public void testValidateSuperclassFields() throws Exception {
        Validation.validate(new Derived());
        
        assertEquals(2, Messages.get().getErrors().size());
        
    }
}
