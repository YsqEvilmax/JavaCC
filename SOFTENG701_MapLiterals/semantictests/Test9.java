public class Test9 {

    public int a;

    class Test9_1 {

        public Test9_1() {
            this.foo();
        }

        public int x;

        public int y;

        public void foo() {
            return this.foo();
            a = 1;
            x = 2;
            y = 3;
        }
    }
}
