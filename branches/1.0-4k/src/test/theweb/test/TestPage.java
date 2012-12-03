package theweb.test;

import theweb.AbstractPage;
import theweb.Page;
import theweb.validation.NotBlank;

public class TestPage {
    @SuppressWarnings("unused")
    public static Page page = new AbstractPage("/") {
        public void action1() {
            
        }
        
        void action2() {
        }
        
        @NotBlank
        public String a;
        
        @NotBlank
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