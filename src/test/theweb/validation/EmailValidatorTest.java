package theweb.validation;

import junit.framework.TestCase;

public class EmailValidatorTest extends TestCase {
    public void testValid() {
        assertTrue(EmailValidator.isValid("bla@bla.com"));
        assertTrue(EmailValidator.isValid("bla.bla@bla.com"));
        assertTrue(EmailValidator.isValid("bla_bla@bla.com"));

        assertFalse(EmailValidator.isValid(null));
        assertFalse(EmailValidator.isValid(""));

        assertFalse(EmailValidator.isValid("bla.com")); //no @
        assertFalse(EmailValidator.isValid("bla@bla@bla.com")); //more that one @
        assertFalse(EmailValidator.isValid("@bla.com")); //no name

        assertFalse(EmailValidator.isValid("Bla bla <bla@bla.com>")); //personal info

        assertFalse(EmailValidator.isValid("katya@.ru")); //wrong domain
        assertFalse(EmailValidator.isValid("katya@.mail.ru")); //wrong domain
        assertFalse(EmailValidator.isValid("54321@fghj/yfcdx")); //wrong domain
        assertFalse(EmailValidator.isValid("nikolini@mail.ru.")); //wrong domain
    }
}
