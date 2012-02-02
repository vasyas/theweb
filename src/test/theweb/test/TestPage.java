package theweb.test;

import theweb.AbstractPage;
import theweb.Page;

public class TestPage {
    @SuppressWarnings("unused")
    public static Page page = new AbstractPage("/") {
        public void action1() {
            
        }
        
        void action2() {
        }
        
        public String a;
        String b;
        
        private String c;
        private String d;
        
        public void setc(String c) {
            this.c = c;
        }
        
        void setd(String d) {
            this.d = d;
        }
        
        public String toString() {
            return "" + a + b + c + d;
        }
    };
}
