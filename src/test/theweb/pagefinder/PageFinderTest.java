package theweb.pagefinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.BasicConfigurator;

import junit.framework.TestCase;
import theweb.Page;
import theweb.pagefinder.pages.onepage.PlainPage;
import theweb.pagefinder.pages.orderstatic.FirstPage;
import theweb.pagefinder.pages.orderstatic.SecondPage;
import theweb.pagefinder.pages.orderstatic.ThirdPage;

public class PageFinderTest extends TestCase {
    public void setUp() throws Exception {
        BasicConfigurator.configure();
    }
    
    public void tearDown() throws Exception {
        BasicConfigurator.resetConfiguration();
    }
    
    List<Page> pages = new ArrayList<Page>();
    
    public void testCantCreate() throws Exception {
        try {
            test("theweb.pagefinder.pages.cantcreate");
            fail();
        } catch(RuntimeException e) {
        }
    }
    
    public void testOnePage() throws Exception {
        test("theweb.pagefinder.pages.onepage");
        
        assertPages(new PlainPage());
    }
    
    public void testOrderStatic() throws Exception {
        test("theweb.pagefinder.pages.orderstatic");
        
        assertPages(new FirstPage(), new SecondPage(), new ThirdPage());
    }
    
    private void assertPages(Page ... pp) {
        assertEquals(Arrays.asList(pp), pages);
    }
    
    private void test(String pkg) {
        PageFinder.create(pkg, new PageFinder.Listener() {
            @Override
            public void pageFound(Page page) {
                pages.add(page);
            } 
        });
    }
}
