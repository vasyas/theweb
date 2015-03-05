package theweb;

public class TestPage {
    @SuppressWarnings("unused")
    public static Page page = new Page("/") {
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
