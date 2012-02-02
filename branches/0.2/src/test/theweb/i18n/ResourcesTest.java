package theweb.i18n;

import theweb.resources.ClasspathResourceLocation;
import junit.framework.TestCase;

public class ResourcesTest extends TestCase {
    
    @Override
    protected void setUp() throws Exception {
        Resources.currentLocale.set(null);
        Resources.localeBundles.clear();
    }
    
    @Override
    protected void tearDown() throws Exception {
        Resources.clearThreadLocalProperties();
    }

    public void testGetText() {
        Resources.addBundle(new ClasspathResourceLocation("theweb/i18n/test_uk.properties"));
        
        Resources.setLocale("uk");
        
        assertEquals("значення", Resources.getText("simple"));
    }
    
    public void testGetTextNoProperty() {
        Resources.setLocale("uk");
        
        assertEquals("simple", Resources.getText("simple"));
    }
    
    public void testGetTextNoLocale() {
        try {
            Resources.getText("text");
            fail();
        } catch(IllegalStateException e) {
        }
    }
    
    public void testGetTextParams() {
        Resources.addBundle(new ClasspathResourceLocation("theweb/i18n/test_uk.properties"));
        
        Resources.setLocale("uk");
        
        assertEquals(Resources.getText("param", "0", "1"), "зна0чення1");
    }
    
    public void testGetTextReferences() {
        Resources.addBundle(new ClasspathResourceLocation("theweb/i18n/test_uk.properties"));
        
        Resources.setLocale("uk");
        
        assertEquals("another значення", Resources.getText("paramWithRef"));
        assertEquals("another referenced.key", Resources.getText("paramWithRefNotFound"));
        
        assertEquals("paramRecursive", Resources.getText("paramRecursive"));
    }
    
    public void testDefaultLocale() {
        Resources.addBundle(new ClasspathResourceLocation("theweb/i18n/test_uk.properties"));

        Resources.setDefaultLocale("uk");
        Resources.setLocale("ru");
        
        assertEquals(Resources.getText("simple"), "значення");
    	
    }
    
    public void testQuotation() {
        Resources.addBundle(new ClasspathResourceLocation("theweb/i18n/test_uk.properties"));
        
        Resources.setLocale("uk");
        
        assertEquals("aa'1", Resources.getText("paramQuote", "1"));
    }
    
    String value;
    public void testTemporaryBundle() throws Exception {
        Resources.addThreadLocalProperty("tl", "value1");
        Resources.setLocale("uk");
        
        assertEquals("value1", Resources.getText("tl"));
        
        Thread t = new Thread() {
            @Override
            public void run() {
                Resources.setLocale("uk");
                value = Resources.getText("tl");
            }
        };
        
        t.start();
        t.join();
        
        assertEquals("tl", value);
    }
    
    public void testBugWithTwoReferences() throws Exception {
        Resources.addBundle(new ClasspathResourceLocation("theweb/i18n/test_uk.properties"));
        
        Resources.setLocale("uk");
        
        assertEquals("Например, <i>1</i> или <i>55</i> 2", Resources.getText("main"));
    }
    
    public void testEscapingBug() throws Exception {
        Resources.addBundle(new ClasspathResourceLocation("theweb/i18n/test_uk.properties"));
        
        Resources.setLocale("uk");
        
        assertEquals("a\\b", Resources.getText("escapingSlash"));
    }
}
